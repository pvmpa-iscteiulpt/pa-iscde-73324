package pt.iscte.pidesco.conventions;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import pt.iscte.pidesco.conventions.service.ConventionsCheckerServices;
import pt.iscte.pidesco.extensibility.PidescoServices;

public class ConventionsCheckerActivator implements BundleActivator {

	private static BundleContext context;
	private static ConventionsCheckerActivator instance;	
	private ServiceRegistration<ConventionsCheckerServices> service;
	private PidescoServices pidescoServices;

	static BundleContext getContext() {
		return context;
	}

	public static ConventionsCheckerActivator getInstance() {
		return instance;
	}
	
	public void start(BundleContext bundleContext) throws Exception {
		ConventionsCheckerActivator.context = bundleContext;
		instance = this;
		service = context.registerService(ConventionsCheckerServices.class, new ConventionsCheckerServicesImpl(), null);

		ServiceReference<PidescoServices> ref = context.getServiceReference(PidescoServices.class);
		pidescoServices = context.getService(ref);
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		instance = null;
		service.unregister();
		ConventionsCheckerActivator.context = null;
	}

	public PidescoServices getPidescoServices() {
		return pidescoServices;
	}

}
