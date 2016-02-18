package com.web.faces.exceptions;

import java.util.Iterator;
//import java.util.Map;
import javax.faces.FacesException;
//import javax.faces.application.NavigationHandler;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

public class ViewExpiredExceptionExceptionHandler extends ExceptionHandlerWrapper {
	private ExceptionHandler wrapped;

	public ViewExpiredExceptionExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	@Override
	public void handle() throws FacesException {
		for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {
			ExceptionQueuedEvent event = i.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
			Throwable t = context.getException();
			if (t instanceof ViewExpiredException) {
				//ViewExpiredException vee = (ViewExpiredException) t;
				//Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
				
				try {
					FacesContext fc = FacesContext.getCurrentInstance();
					// Push some useful stuff to the request scope for use in the page
					//requestMap.put("currentViewId", vee.getViewId());
					System.err.println("Timeout");
					NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();
					navigationHandler.handleNavigation(fc, null, "not_logged?faces-redirect=true");
					//new FacesUtil().redirect("not_logged.jsf?faces-redirect=true");
					//fc.renderResponse();

				} finally {
					i.remove();
				}
			}
		}
		// At this point, the queue will not contain any ViewExpiredEvents.
		// Therefore, let the parent handle them.
		getWrapped().handle();

	}
}