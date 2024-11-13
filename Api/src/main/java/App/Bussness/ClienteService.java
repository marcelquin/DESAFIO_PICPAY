package App.Bussness;

import App.Domain.ClienteResponse;
import App.Domain.TransferenciaEnviadaDto;
import App.Domain.TransferenciaRecebidaDto;
import App.Infra.Exceptions.EntityNotFoundException;
import App.Infra.Exceptions.IllegalActionException;
import App.Infra.Exceptions.NullargumentsException;
import App.Infra.Gateway.ClienteGateway;
import App.Infra.Persistence.Entity.*;
import App.Infra.Persistence.Enum.TIPOCADASTRO;
import App.Infra.Persistence.Repository.ClienteRepository;
import App.Infra.Persistence.Repository.ContaRepository;
import App.Infra.Persistence.Repository.TransferenciaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ClienteService implements ClienteGateway {

    private final ClienteRepository clienteRepository;
    private final ContaRepository contaRepository;
    private final TransferenciaRepository transferenciaRepository;

    Locale localBrasil = new Locale("pt", "BR");

    public ClienteService(ClienteRepository clienteRepository, ContaRepository contaRepository, TransferenciaRepository transferenciaRepository) {
        this.clienteRepository = clienteRepository;
        this.contaRepository = contaRepository;
        this.transferenciaRepository = transferenciaRepository;
    }
    //NumberFormat.getCurrencyInstance(localBrasil).format(),


    @Override
    public ResponseEntity<List<ClienteResponse>> ListarCLientes()
    {
        try
        {
            List<ClienteEntity> entities = clienteRepository.findAll();
            List<ClienteResponse> response = new ArrayList<>();
            for(ClienteEntity clienteEntity : entities)
            {
                List<TransferenciaRecebidaDto> transferenciaRecebidaEntities = new ArrayList<>();
                List<TransferenciaEnviadaDto> transferenciaEnviadaEntities = new ArrayList<>();
                if(clienteEntity.getContaEntity().getTransferencia().getTransferenciaEnviadaEntities().size() > 0)
                {
                    for(TransferenciaEnviadaEntity enviadaEntity : clienteEntity.getContaEntity().getTransferencia().getTransferenciaEnviadaEntities())
                    {
                        TransferenciaEnviadaDto dto = new TransferenciaEnviadaDto(enviadaEntity.getPayer(),
                                enviadaEntity.getEmailPayee(),
                                enviadaEntity.getPayee(),
                                enviadaEntity.getEmailPayee(),
                                enviadaEntity.getCodigo(),
                                NumberFormat.getCurrencyInstance(localBrasil).format(enviadaEntity.getValor()),
                                enviadaEntity.getTimeStamp(),
                                enviadaEntity.getStatustransferencia());
                        transferenciaEnviadaEntities.add(dto);
                    }
                }
                if(clienteEntity.getContaEntity().getTransferencia().getTransferenciaRecebidaEntities().size() > 0) {
                    for (TransferenciaRecebidaEntity recebidaEntity : clienteEntity.getContaEntity().getTransferencia().getTransferenciaRecebidaEntities())
                    {
                        TransferenciaRecebidaDto dto = new TransferenciaRecebidaDto(recebidaEntity.getPayer(),
                                recebidaEntity.getEmailPayee(),
                                recebidaEntity.getPayee(),
                                recebidaEntity.getEmailPayee(),
                                recebidaEntity.getCodigo(),
                                NumberFormat.getCurrencyInstance(localBrasil).format(recebidaEntity.getValor()),
                                recebidaEntity.getTimeStamp(),
                                recebidaEntity.getStatustransferencia());
                        transferenciaRecebidaEntities.add(dto);
                    }
                }
                ClienteResponse dto = new ClienteResponse(clienteEntity.getId(),
                                                          clienteEntity.getNomeCompleto(),
                                                          clienteEntity.getDocumento(),
                                                          clienteEntity.getEmail(),
                                                          NumberFormat.getCurrencyInstance(localBrasil).format(clienteEntity.getContaEntity().getSaldo()),
                                                          clienteEntity.getTipocadastro(),
                                                          transferenciaEnviadaEntities,
                                                          transferenciaRecebidaEntities);
                response.add(dto);
            }
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    public ResponseEntity<ClienteResponse> BuscarClientePorId(Long idCliente)
    {
        try
        {
            if(idCliente != null)
            {
                ClienteEntity clienteEntity = clienteRepository.findById(idCliente).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                List<TransferenciaRecebidaDto> transferenciaRecebidaEntities = new ArrayList<>();
                List<TransferenciaEnviadaDto> transferenciaEnviadaEntities = new ArrayList<>();
                if(clienteEntity.getContaEntity().getTransferencia().getTransferenciaEnviadaEntities().size() > 0)
                {
                    for(TransferenciaEnviadaEntity enviadaEntity : clienteEntity.getContaEntity().getTransferencia().getTransferenciaEnviadaEntities())
                    {
                        TransferenciaEnviadaDto dto = new TransferenciaEnviadaDto(enviadaEntity.getPayer(),
                                enviadaEntity.getEmailPayee(),
                                enviadaEntity.getPayee(),
                                enviadaEntity.getEmailPayee(),
                                enviadaEntity.getCodigo(),
                                NumberFormat.getCurrencyInstance(localBrasil).format(enviadaEntity.getValor()),
                                enviadaEntity.getTimeStamp(),
                                enviadaEntity.getStatustransferencia());
                        transferenciaEnviadaEntities.add(dto);
                    }
                }
                if(clienteEntity.getContaEntity().getTransferencia().getTransferenciaRecebidaEntities().size() > 0) {
                    for (TransferenciaRecebidaEntity recebidaEntity : clienteEntity.getContaEntity().getTransferencia().getTransferenciaRecebidaEntities())
                    {
                        TransferenciaRecebidaDto dto = new TransferenciaRecebidaDto(recebidaEntity.getPayer(),
                                recebidaEntity.getEmailPayee(),
                                recebidaEntity.getPayee(),
                                recebidaEntity.getEmailPayee(),
                                recebidaEntity.getCodigo(),
                                NumberFormat.getCurrencyInstance(localBrasil).format(recebidaEntity.getValor()),
                                recebidaEntity.getTimeStamp(),
                                recebidaEntity.getStatustransferencia());
                        transferenciaRecebidaEntities.add(dto);
                    }
                }
                ClienteResponse response = new ClienteResponse(clienteEntity.getId(),
                        clienteEntity.getNomeCompleto(),
                        clienteEntity.getDocumento(),
                        clienteEntity.getEmail(),
                        NumberFormat.getCurrencyInstance(localBrasil).format(clienteEntity.getContaEntity().getSaldo()),
                        clienteEntity.getTipocadastro(),
                        transferenciaEnviadaEntities,
                        transferenciaRecebidaEntities);
                return new ResponseEntity<>(response, HttpStatus.OK);
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

    @Override
    public ResponseEntity<ClienteResponse> NovoCLiente(String nome,
                                                       String cpjCnpj,
                                                       String email,
                                                       Long senha,
                                                       TIPOCADASTRO tipoCadastro)
    {
        try
        {
            if(senha == 0)
            { throw new IllegalActionException("Valor invalido");}
            if(nome != null &&
               cpjCnpj != null &&
               email != null &&
               senha != null)
            {
                ClienteEntity clienteEntity = new ClienteEntity();
                int conta = (int) (111111 + Math.random() * 999999);
                ContaEntity contaEntity = new ContaEntity();
                contaEntity.setSaldo(50.0);
                contaEntity.setAgencia(1234L);
                contaEntity.setConta((long) conta);
                contaEntity.setSenhaTransacao(senha);
                contaEntity.setTimeStamp(LocalDateTime.now());
                TransferenciaEntity transferenciaEntity = new TransferenciaEntity();
                transferenciaEntity.setTimeStamp(LocalDateTime.now());
                transferenciaRepository.save(transferenciaEntity);
                contaEntity.setTransferencia(transferenciaEntity);
                contaRepository.save(contaEntity);
                clienteEntity.setContaEntity(contaEntity);
                clienteEntity.setNomeCompleto(nome);
                clienteEntity.setDocumento(cpjCnpj);
                clienteEntity.setEmail(email);
                clienteEntity.setTipocadastro(tipoCadastro);
                clienteEntity.setTimeStamp(LocalDateTime.now());
                clienteRepository.save(clienteEntity);
                ClienteResponse response = new ClienteResponse(clienteEntity.getId(),
                                                                clienteEntity.getNomeCompleto(),
                                                                clienteEntity.getDocumento(),
                                                                clienteEntity.getEmail(),
                                                                NumberFormat.getCurrencyInstance(localBrasil).format(clienteEntity.getContaEntity().getSaldo()),
                                                                clienteEntity.getTipocadastro(),null,null);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
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

    @Override
    public ResponseEntity<ClienteResponse> EditarCLiente(Long idCliente,
                                                         String nome,
                                                         String cpjCnpj,
                                                         String email,
                                                         TIPOCADASTRO tipoCadastro)
    {
        try
        {
            if(idCliente != null &&
               nome != null &&
               cpjCnpj != null &&
               email != null)
            {
                ClienteEntity clienteEntity = clienteRepository.findById(idCliente).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                clienteEntity.setNomeCompleto(nome);
                clienteEntity.setEmail(email);
                clienteEntity.setDocumento(cpjCnpj);
                clienteEntity.setTipocadastro(tipoCadastro);
                clienteRepository.save(clienteEntity);
                List<TransferenciaRecebidaDto> transferenciaRecebidaEntities = new ArrayList<>();
                List<TransferenciaEnviadaDto> transferenciaEnviadaEntities = new ArrayList<>();
                if(clienteEntity.getContaEntity().getTransferencia().getTransferenciaEnviadaEntities().size() > 0)
                {
                    for(TransferenciaEnviadaEntity enviadaEntity : clienteEntity.getContaEntity().getTransferencia().getTransferenciaEnviadaEntities())
                    {
                        TransferenciaEnviadaDto dto = new TransferenciaEnviadaDto(enviadaEntity.getPayer(),
                                enviadaEntity.getEmailPayee(),
                                enviadaEntity.getPayee(),
                                enviadaEntity.getEmailPayee(),
                                enviadaEntity.getCodigo(),
                                NumberFormat.getCurrencyInstance(localBrasil).format(enviadaEntity.getValor()),
                                enviadaEntity.getTimeStamp(),
                                enviadaEntity.getStatustransferencia());
                        transferenciaEnviadaEntities.add(dto);
                    }
                }
                if(clienteEntity.getContaEntity().getTransferencia().getTransferenciaRecebidaEntities().size() > 0) {
                    for (TransferenciaRecebidaEntity recebidaEntity : clienteEntity.getContaEntity().getTransferencia().getTransferenciaRecebidaEntities())
                    {
                        TransferenciaRecebidaDto dto = new TransferenciaRecebidaDto(recebidaEntity.getPayer(),
                                recebidaEntity.getEmailPayee(),
                                recebidaEntity.getPayee(),
                                recebidaEntity.getEmailPayee(),
                                recebidaEntity.getCodigo(),
                                NumberFormat.getCurrencyInstance(localBrasil).format(recebidaEntity.getValor()),
                                recebidaEntity.getTimeStamp(),
                                recebidaEntity.getStatustransferencia());
                        transferenciaRecebidaEntities.add(dto);
                    }
                }
                ClienteResponse response = new ClienteResponse(clienteEntity.getId(),
                                                                clienteEntity.getNomeCompleto(),
                                                                clienteEntity.getDocumento(),
                                                                clienteEntity.getEmail(),
                                                                NumberFormat.getCurrencyInstance(localBrasil).format(clienteEntity.getContaEntity().getSaldo()),
                                                                clienteEntity.getTipocadastro(),
                                                                transferenciaEnviadaEntities,
                                                                transferenciaRecebidaEntities);
                return new ResponseEntity<>(response, HttpStatus.OK);
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

    @Override
    public ResponseEntity<ClienteResponse> DeletarCliente(Long idCliente)
    {
        try
        {
            if(idCliente != null)
            {
                /*ClienteEntity entity = clienteRepository.findById(idCliente).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                loginRepository.deleteById(entity.getLoginEntity().getId());
                clienteRepository.deleteById(entity.getId());*/
                return new ResponseEntity<>(HttpStatus.OK);
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
