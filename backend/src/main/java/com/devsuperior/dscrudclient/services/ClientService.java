package com.devsuperior.dscrudclient.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscrudclient.dto.ClientDTO;
import com.devsuperior.dscrudclient.entities.Client;
import com.devsuperior.dscrudclient.repositories.ClientRepository;
import com.devsuperior.dscrudclient.services.exceptions.DataBaseException;
import com.devsuperior.dscrudclient.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAll(PageRequest pageRequest) {
		return repository.findAll(pageRequest).map(entity -> new ClientDTO(entity));

	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		return new ClientDTO(findEntityExists(id));

	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		this.copyDtoToEntity(dto, repository.save(entity));
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO update(ClientDTO dto, Long id) {
		try {
			Client entity = repository.getOne(id);
			this.copyDtoToEntity(dto, entity);
			return new ClientDTO(repository.save(entity));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.delete(this.findEntityExists(id));
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}

	}

	private Client findEntityExists(Long id) {
		Optional<Client> obj = repository.findById(id);
		obj.orElseThrow(() -> new ResourceNotFoundException("Id not found " + id));
		return obj.get();
	}

	private void copyDtoToEntity(ClientDTO dto, Client entity) {
		entity.setName(dto.getName());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
	}
}
