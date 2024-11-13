package App.Bussness;

import App.Domain.ClienteResponse;
import App.Domain.TransferenciaEnviadaDto;
import App.Domain.TransferenciaRecebidaDto;
import App.Infra.Exceptions.EntityNotFoundException;
import App.Infra.Exceptions.NullargumentsException;
import App.Infra.Gateway.ClienteGateway;
import App.Infra.Persistence.Entity.*;
import App.Infra.Persistence.Enum.TIPOCADASTRO;
import App.Infra.Persistence.Repository.*;
import App.Security.Controller.AuthenticationController;
import App.Security.DTO.RegisterDTO;
import App.Security.Model.UserRole;
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
    private final TransferenciaEnviadaRepository transferenciaEnviadaRepository;
    private final TransferenciaRecebidaRepository transferenciaRecebidaRepository;
    private final AuthenticationController authenticationController;

    Locale localBrasil = new Locale("pt", "BR");

    public ClienteService(ClienteRepository clienteRepository, ContaRepository contaRepository, TransferenciaRepository transferenciaRepository, TransferenciaEnviadaRepository transferenciaEnviadaRepository, TransferenciaRecebidaRepository transferenciaRecebidaRepository, AuthenticationController authenticationController) {
        this.clienteRepository = clienteRepository;
        this.contaRepository = contaRepository;
        this.transferenciaRepository = transferenciaRepository;
        this.transferenciaEnviadaRepository = transferenciaEnviadaRepository;
        this.transferenciaRecebidaRepository = transferenciaRecebidaRepository;
        this.authenticationController = authenticationController;
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
                                                       String senha,
                                                       TIPOCADASTRO tipoCadastro)
    {
        try
        {
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
                //aqui
                RegisterDTO registerDTO = new RegisterDTO(clienteEntity.getEmail(),senha, UserRole.ADMIN);
                //
                authenticationController.register(registerDTO);
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
            if(idCliente != null) {
                ClienteEntity entity = clienteRepository.findById(idCliente).orElseThrow(
                        () -> new EntityNotFoundException()
                );
                TransferenciaEntity transferenciaEntity = transferenciaRepository.findById(entity.getContaEntity().getTransferencia().getId()).orElseThrow(
                        () -> new EntityNotFoundException()
                );
                for (TransferenciaRecebidaEntity recebidaEntity : transferenciaEntity.getTransferenciaRecebidaEntities())
                {
                    transferenciaRecebidaRepository.deleteById(recebidaEntity.getId());
                }
                for(TransferenciaEnviadaEntity enviadaEntity : transferenciaEntity.getTransferenciaEnviadaEntities())
                {
                    transferenciaEnviadaRepository.deleteById(enviadaEntity.getId());
                }
                transferenciaRepository.deleteById(entity.getContaEntity().getTransferencia().getId());
                contaRepository.deleteById(entity.getContaEntity().getId());
                clienteRepository.deleteById(entity.getId());
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
