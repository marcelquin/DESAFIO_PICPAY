package App.Bussness;

import App.Domain.Response;
import App.Domain.TransferenciaResponse;
import App.Infra.Exceptions.EntityNotFoundException;
import App.Infra.Exceptions.IllegalActionException;
import App.Infra.Exceptions.NullargumentsException;
import App.Infra.Gateway.TransferenciaGateway;
import App.Infra.Persistence.Entity.*;
import App.Infra.Persistence.Enum.STATUSTRANSFERENCIA;
import App.Infra.Persistence.Enum.TIPOCADASTRO;
import App.Infra.Persistence.Repository.*;
import App.Producer.RequestTransferenciaProducer;
import App.Security.Controller.AuthenticationController;
import App.Security.DTO.AuthenticationDTO;
import App.Security.DTO.LoginResponseDTO;
import App.Security.Util.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class TransferenciaService implements TransferenciaGateway {


    private final ClienteRepository clienteRepository;
    private final TransferenciaRepository transferenciaRepository;
    private final TransferenciaEnviadaRepository transferenciaEnviadaRepository;
    private final TransferenciaRecebidaRepository transferenciaRecebidaRepository;
    private final RequestTransferenciaProducer producer;
    private final AuthenticationController authenticationController;
    private final ContaRepository contaRepository;
    private TokenService tokenService;
    Locale localBrasil = new Locale("pt", "BR");

    public TransferenciaService(ClienteRepository clienteRepository, TransferenciaRepository transferenciaRepository, TransferenciaEnviadaRepository transferenciaEnviadaRepository, TransferenciaRecebidaRepository transferenciaRecebidaRepository, RequestTransferenciaProducer producer, AuthenticationController authenticationController, ContaRepository contaRepository) {
        this.clienteRepository = clienteRepository;
        this.transferenciaRepository = transferenciaRepository;
        this.transferenciaEnviadaRepository = transferenciaEnviadaRepository;
        this.transferenciaRecebidaRepository = transferenciaRecebidaRepository;
        this.producer = producer;
        this.authenticationController = authenticationController;
        this.contaRepository = contaRepository;
    }

    @Override
    public ResponseEntity<TransferenciaResponse> novaTransferencia(Long idPayer,
                                                                   String senha,
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
                    AuthenticationDTO dto = new AuthenticationDTO(payer.getEmail(),senha);
                    LoginResponseDTO loginResponseDTO = authenticationController.login(dto).getBody();
                    System.out.println(loginResponseDTO);
                    if(loginResponseDTO.token() != null)
                    {
                        if(valor < payer.getContaEntity().getSaldo())
                        {
                            int codigo = (int) (111111 + Math.random() * 999999);
                            ClienteEntity payee = clienteRepository.findById(idPayee).orElseThrow(
                                    ()-> new EntityNotFoundException()
                            );
                            TransferenciaEntity transferenciaEntitypayee = transferenciaRepository.findById(payee.getContaEntity().getTransferencia().getId()).orElseThrow(
                                    ()-> new EntityNotFoundException()
                            );
                            TransferenciaEntity transferenciaEntitypayer = transferenciaRepository.findById(payer.getContaEntity().getTransferencia().getId()).orElseThrow(
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
                            transferenciaEntitypayer.getTransferenciaEnviadaEntities().add(transferenciaEnviada);
                            transferenciaEntitypayee.getTransferenciaRecebidaEntities().add(transferenciaRecebida);
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



    @Override
    public ResponseEntity<Response> SaqueValor(Long idPayer,
                                               String senha,
                                               Double valor)
    {
        try
        {
            if(valor < 0){throw  new IllegalActionException("Valor Invalido");}
            if(idPayer != null &&
                    senha != null &&
                    valor != null)
            {
                ClienteEntity clienteEntity = clienteRepository.findById(idPayer).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                AuthenticationDTO dto = new AuthenticationDTO(clienteEntity.getEmail(),senha);
                LoginResponseDTO loginResponseDTO = authenticationController.login(dto).getBody();
                System.out.println(loginResponseDTO);
                if(loginResponseDTO.token() != null)
                {
                    ContaEntity contaEntity = contaRepository.findById(clienteEntity.getContaEntity().getId()).orElseThrow(
                            ()-> new EntityNotFoundException()
                    );
                    contaEntity.setSaldo(contaEntity.getSaldo() - valor);
                    contaEntity.setTimeStamp(LocalDateTime.now());
                    contaRepository.save(contaEntity);
                    Response response = new Response(clienteEntity.getNomeCompleto(),
                            clienteEntity.getEmail(),
                            valor,
                            LocalDateTime.now());
                    return new ResponseEntity<>(response,HttpStatus.OK);
                }
                else
                {throw new IllegalActionException("Acesso não autorizado");}
            }
            else
            {throw new NullargumentsException();}
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    public ResponseEntity<Response> DepositoValor(Long idPayer,
                                                  String senha,
                                                  Double valor)
    {
        try
        {
            if(valor < 0){throw  new IllegalActionException("Valor Invalido");}
            if(idPayer != null &&
                    senha != null &&
                    valor != null)
            {
                ClienteEntity clienteEntity = clienteRepository.findById(idPayer).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                AuthenticationDTO dto = new AuthenticationDTO(clienteEntity.getEmail(),senha);
                LoginResponseDTO loginResponseDTO = authenticationController.login(dto).getBody();
                System.out.println(loginResponseDTO);
                if(loginResponseDTO.token() != null)
                {
                    ContaEntity contaEntity = contaRepository.findById(clienteEntity.getContaEntity().getId()).orElseThrow(
                            ()-> new EntityNotFoundException()
                    );
                    contaEntity.setSaldo(contaEntity.getSaldo() + valor);
                    contaEntity.setTimeStamp(LocalDateTime.now());
                    contaRepository.save(contaEntity);
                    Response response = new Response(clienteEntity.getNomeCompleto(),
                                                     clienteEntity.getEmail(),
                                                     valor,
                                                     LocalDateTime.now());
                    return new ResponseEntity<>(response,HttpStatus.OK);
                }
                else
                {throw new IllegalActionException("Acesso não autorizado");}
            }
            else
            {throw new NullargumentsException();}
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
