package pt.iscte.pidesco.conventions.minimap;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import pt.iscte.pidesco.conventions.service.ConventionsCheckerServices;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static ConventionsCheckerServices conventionsCheckerServices;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		initializeConventionsCheckerServices();
	}

	private void initializeConventionsCheckerServices() {
		ServiceReference<ConventionsCheckerServices> serviceReference = context.getServiceReference(ConventionsCheckerServices.class);
		Activator.conventionsCheckerServices = context.getService(serviceReference);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	public static ConventionsCheckerServices getConventionsCheckerServices() {
		return Activator.conventionsCheckerServices;
	}
}
