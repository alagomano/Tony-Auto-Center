package model.exception;

import model.services.ServiceItem;

public class ServiceException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ServiceException(String msg){
        super(msg);
    }

}
