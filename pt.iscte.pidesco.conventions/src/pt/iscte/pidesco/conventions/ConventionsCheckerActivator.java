package pt.iscte.pidesco.conventions;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import pt.iscte.pidesco.conventions.service.ConventionsCheckerServices;
import pt.iscte.pidesco.extensibility.PidescoServices;

/**
 * Handles the activation of the Conventions Checker plug-in for PIDESCO.
 * 
 * @author grovy
 *
 */
public class ConventionsCheckerActivator implements BundleActivator {

	private static BundleContext context;
	private static ConventionsCheckerActivator instance;
	private ServiceRegistration<ConventionsCheckerServices> service;
	private PidescoServices pidescoServices;

	/**
	 * Returns the bundle context.
	 * 
	 * @return the bundle context
	 */
	static BundleContext getContext() {
		return context;
	}

	/**
	 * Returns the singleton instance of the Conventions Checker Activator.
	 * 
	 * @return singleton instance of the activator
	 */
	public static ConventionsCheckerActivator getInstance() {
		return instance;
	}


	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ConventionsCheckerActivator.context = bundleContext;
		instance = this;
		service = context.registerService(ConventionsCheckerServices.class, new ConventionsCheckerServicesImpl(), null);

		ServiceReference<PidescoServices> ref = context.getServiceReference(PidescoServices.class);
		pidescoServices = context.getService(ref);

	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		instance = null;
		service.unregister();
		ConventionsCheckerActivator.context = null;
	}

	/**
	 * Returns an instance of a PIDESCO services implementation. Used to access
	 * PIDESCO's services in a consistent matter that doesn't require the View to be
	 * active.
	 * 
	 * @return an instance of a PIDESCO services implementation.
	 */
	public PidescoServices getPidescoServices() {
		return pidescoServices;
	}

}
