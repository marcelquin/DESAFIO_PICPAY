package App.Bussness;

import App.Domain.TransferenciaResponse;
import App.Infra.Exceptions.EntityNotFoundException;
import App.Infra.Exceptions.IllegalActionException;
import App.Infra.Exceptions.NullargumentsException;
import App.Infra.Gateway.TransferenciaGateway;
import App.Infra.Persistence.Entity.ClienteEntity;
import App.Infra.Persistence.Entity.TransferenciaEnviadaEntity;
import App.Infra.Persistence.Entity.TransferenciaRecebidaEntity;
import App.Infra.Persistence.Enum.STATUSTRANSFERENCIA;
import App.Infra.Persistence.Enum.TIPOCADASTRO;
import App.Infra.Persistence.Repository.ClienteRepository;
import App.Infra.Persistence.Repository.LoginRepository;
import App.Infra.Persistence.Repository.TransferenciaEnviadaRepository;
import App.Infra.Persistence.Repository.TransferenciaRecebidaRepository;
import App.Producer.RequestTransferenciaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class TransferenciaService implements TransferenciaGateway {


    private final ClienteRepository clienteRepository;
    private final TransferenciaEnviadaRepository transferenciaEnviadaRepository;
    private final TransferenciaRecebidaRepository transferenciaRecebidaRepository;
    private final LoginRepository loginRepository;

    private final RequestTransferenciaProducer producer;
    Locale localBrasil = new Locale("pt", "BR");
    public TransferenciaService(ClienteRepository clienteRepository, TransferenciaEnviadaRepository transferenciaEnviadaRepository, TransferenciaRecebidaRepository transferenciaRecebidaRepository, LoginRepository loginRepository, RequestTransferenciaProducer producer) {
        this.clienteRepository = clienteRepository;
        this.transferenciaEnviadaRepository = transferenciaEnviadaRepository;
        this.transferenciaRecebidaRepository = transferenciaRecebidaRepository;
        this.loginRepository = loginRepository;
        this.producer = producer;
    }

    @Override
    public ResponseEntity<TransferenciaResponse> novaTransferencia(Long idPayer,
                                                                   Long idPayee,
                                                                   Double valor)
    {
        {
            try
            {
                if(valor < 0){throw  new IllegalActionException("Valor Invalido");}
                if(idPayee != null &&
                   idPayer != null &&
                   valor != null)
                {
                    ClienteEntity payer = clienteRepository.findById(idPayer).orElseThrow(
                            ()-> new EntityNotFoundException()
                    );
                    if(payer.getTipocadastro() == TIPOCADASTRO.EMPRESA)
                    {throw new IllegalActionException("Ação não permitida a logistas.");}
                    if(valor < payer.getSaldo())
                    {
                        int codigo = (int) (111111 + Math.random() * 999999);
                        ClienteEntity payee = clienteRepository.findById(idPayee).orElseThrow(
                                ()-> new EntityNotFoundException()
                        );
                        TransferenciaEnviadaEntity transferenciaEnviada = new TransferenciaEnviadaEntity();
                        transferenciaEnviada.setPayer(payer.getNomeCompleto());
                        transferenciaEnviada.setEmailPayer(payer.getEmail());
                        transferenciaEnviada.setPayee(payee.getNomeCompleto());
                        transferenciaEnviada.setEmailPayee(payee.getEmail());
                        transferenciaEnviada.setValor(valor);
                        transferenciaEnviada.setTimeStamp(LocalDateTime.now());
                        transferenciaEnviada.setCodigo("tr_"+codigo);
                        transferenciaEnviada.setStatustransferencia(STATUSTRANSFERENCIA.AGUARDANDO);
                        TransferenciaRecebidaEntity transferenciaRecebida = new TransferenciaRecebidaEntity();
                        transferenciaRecebida.setPayer(payer.getNomeCompleto());
                        transferenciaRecebida.setEmailPayer(payer.getEmail());
                        transferenciaRecebida.setPayee(payee.getNomeCompleto());
                        transferenciaRecebida.setEmailPayee(payee.getEmail());
                        transferenciaRecebida.setValor(valor);
                        transferenciaRecebida.setTimeStamp(LocalDateTime.now());
                        transferenciaRecebida.setCodigo("tr_"+codigo);
                        transferenciaRecebida.setStatustransferencia(STATUSTRANSFERENCIA.AGUARDANDO);
                        transferenciaEnviadaRepository.save(transferenciaEnviada);
                        transferenciaRecebidaRepository.save(transferenciaRecebida);
                        payer.getTransferenciaEnviadaEntities().add(transferenciaEnviada);
                        payee.getTransferenciaRecebidaEntities().add(transferenciaRecebida);
                        clienteRepository.save(payer);
                        clienteRepository.save(payee);
                        TransferenciaResponse response = new TransferenciaResponse(payer.getNomeCompleto(),
                                payer.getEmail(),
                                payee.getNomeCompleto(),
                                payee.getEmail(),
                                transferenciaEnviada.getCodigo(),
                                valor);
                        producer.integrar(response);
                        return new ResponseEntity<>(response,HttpStatus.OK);
                    }
                    else
                    { throw new IllegalActionException("Saldo insuficiente.");}
                }
                else
                { throw new NullargumentsException();}
            }
            catch (Exception e)
            {
                e.getMessage();
            }
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }


    public void ConcluirTransacao(String emailPayer,
                                  String emailPayee,
                                  String codigoTransacao)
    {
        try
        {
            TransferenciaEnviadaEntity transferenciaEnviada = transferenciaEnviadaRepository.findBycodigo(codigoTransacao).orElseThrow(
                    ()-> new EntityNotFoundException()
            );
            TransferenciaRecebidaEntity transferenciaRecebida = transferenciaRecebidaRepository.findBycodigo(codigoTransacao).orElseThrow(
                    ()-> new EntityNotFoundException()
            );
            ClienteEntity payer = clienteRepository.findByemail(emailPayer).orElseThrow(
                    ()-> new EntityNotFoundException()
            );
            System.out.println("saldo usuario: "+payer.getSaldo());
            System.out.println("valor transferencia:+ "+transferenciaEnviada.getValor());
            if(payer.getSaldo() > transferenciaEnviada.getValor() )
            {
                ClienteEntity payee = clienteRepository.findByemail(emailPayee).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                payer.setSaldo(payer.getSaldo() - transferenciaEnviada.getValor());
                payee.setSaldo(payee.getSaldo() + transferenciaEnviada.getValor());
                System.out.println("saldo recebido: "+ payee.getSaldo());
                payee.setTimeStamp(LocalDateTime.now());
                payer.setTimeStamp(LocalDateTime.now());
                transferenciaRecebida.setStatustransferencia(STATUSTRANSFERENCIA.AUTORIZADA);
                transferenciaRecebida.setTimeStamp(LocalDateTime.now());
                transferenciaEnviada.setStatustransferencia(STATUSTRANSFERENCIA.AUTORIZADA);
                transferenciaEnviada.setTimeStamp(LocalDateTime.now());
                transferenciaRecebidaRepository.save(transferenciaRecebida);
                transferenciaEnviadaRepository.save(transferenciaEnviada);
                clienteRepository.save(payee);
                clienteRepository.save(payer);
            }
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }

}
