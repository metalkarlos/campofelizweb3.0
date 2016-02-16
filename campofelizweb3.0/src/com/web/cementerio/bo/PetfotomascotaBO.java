package com.web.cementerio.bo;

import com.web.cementerio.dao.PetfotomascotaDAO;

public class PetfotomascotaBO {
	PetfotomascotaDAO petfotomascotaDAO = null;
	
	public PetfotomascotaBO(){
		petfotomascotaDAO = new PetfotomascotaDAO();
	}
	
	public PetfotomascotaDAO getPetfotomascotaDAO() {
		return petfotomascotaDAO;
	}

	public void setPetfotomascotaDAO(PetfotomascotaDAO petfotomascotaDAO) {
		this.petfotomascotaDAO = petfotomascotaDAO;
	}

	
	
}
