package App.Bussness;

import App.Domain.ClienteResponse;
import App.Infra.Exceptions.EntityNotFoundException;
import App.Infra.Exceptions.IllegalActionException;
import App.Infra.Exceptions.NullargumentsException;
import App.Infra.Gateway.ClienteGateway;
import App.Infra.Persistence.Entity.ClienteEntity;
import App.Infra.Persistence.Entity.LoginEntity;
import App.Infra.Persistence.Enum.TIPOCADASTRO;
import App.Infra.Persistence.Repository.ClienteRepository;
import App.Infra.Persistence.Repository.LoginRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ClienteService implements ClienteGateway {

    private final ClienteRepository clienteRepository;
    private final LoginRepository loginRepository;

    Locale localBrasil = new Locale("pt", "BR");
    //NumberFormat.getCurrencyInstance(localBrasil).format(),
    public ClienteService(ClienteRepository clienteRepository, LoginRepository loginRepository) {
        this.clienteRepository = clienteRepository;
        this.loginRepository = loginRepository;
    }

    @Override
    public ResponseEntity<List<ClienteResponse>> ListarCLientes()
    {
        try
        {
            List<ClienteEntity> entities = clienteRepository.findAll();
            List<ClienteResponse> response = new ArrayList<>();
            for(ClienteEntity entity : entities)
            {
                ClienteResponse dto = new ClienteResponse(entity.getNomeCompleto(),
                                                        entity.getCpfCnpj(),
                                                        entity.getEmail(),
                                                        NumberFormat.getCurrencyInstance(localBrasil).format(entity.getSaldo()),
                                                        entity.getTipocadastro());
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
                ClienteEntity entity = clienteRepository.findById(idCliente).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                ClienteResponse response = new ClienteResponse(entity.getNomeCompleto(),
                                                                entity.getCpfCnpj(),
                                                                entity.getEmail(),
                                                                NumberFormat.getCurrencyInstance(localBrasil).format(entity.getSaldo()),
                                                                entity.getTipocadastro());
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
            if(senha.length() > 8)
            { throw new IllegalActionException("a senha deve conter 8 digitos");}
            if(nome != null &&
               cpjCnpj != null &&
               email != null &&
               senha != null)
            {
                LoginEntity loginEntity = new LoginEntity();
                ClienteEntity clienteEntity = new ClienteEntity();
                loginEntity.setLogin(email);
                loginEntity.setPassword(senha);
                System.out.println(loginEntity.getPassword());
                loginEntity.setTimeStamp(LocalDateTime.now());
                loginRepository.save(loginEntity);
                clienteEntity.setLoginEntity(loginEntity);
                clienteEntity.setNomeCompleto(nome);
                clienteEntity.setCpfCnpj(cpjCnpj);
                clienteEntity.setEmail(email);
                clienteEntity.setSaldo(0.0);
                clienteEntity.setTipocadastro(tipoCadastro);
                clienteEntity.setTimeStamp(LocalDateTime.now());
                clienteRepository.save(clienteEntity);
                ClienteResponse response = new ClienteResponse(clienteEntity.getNomeCompleto(),
                                                                clienteEntity.getCpfCnpj(),
                                                                clienteEntity.getEmail(),
                                                                NumberFormat.getCurrencyInstance(localBrasil).format(clienteEntity.getSaldo()),
                                                                clienteEntity.getTipocadastro());
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
                                                         String senha,
                                                         TIPOCADASTRO tipoCadastro)
    {
        try
        {
            if(idCliente != null &&
               nome != null &&
               cpjCnpj != null &&
               email != null &&
               senha != null)
            {
                ClienteEntity clienteEntity = clienteRepository.findById(idCliente).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                LoginEntity loginEntity = loginRepository.findById(clienteEntity.getLoginEntity().getId()).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                loginEntity.setLogin(email);
                loginEntity.setPassword(senha);
                loginEntity.setTimeStamp(LocalDateTime.now());
                loginRepository.save(loginEntity);
                clienteEntity.setNomeCompleto(nome);
                clienteEntity.setEmail(email);
                clienteEntity.setCpfCnpj(cpjCnpj);
                clienteEntity.setTipocadastro(tipoCadastro);
                clienteRepository.save(clienteEntity);
                ClienteResponse response = new ClienteResponse(clienteEntity.getNomeCompleto(),
                                                                clienteEntity.getCpfCnpj(),
                                                                clienteEntity.getEmail(),
                                                                NumberFormat.getCurrencyInstance(localBrasil).format(clienteEntity.getSaldo()),
                                                                clienteEntity.getTipocadastro());
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
                ClienteEntity entity = clienteRepository.findById(idCliente).orElseThrow(
                        ()-> new EntityNotFoundException()
                );
                loginRepository.deleteById(entity.getLoginEntity().getId());
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
