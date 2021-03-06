package vdi.webinterface.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.resteasy.client.ProxyFactory;

import vdi.commons.common.Configuration;
import vdi.commons.common.RESTEasyClientExecutor;
import vdi.commons.common.Util;
import vdi.commons.common.objects.VirtualMachineStatus;
import vdi.commons.web.rest.interfaces.ManagementVMService;
import vdi.commons.web.rest.objects.ManagementVM;

/**
 * Servlet implementation class RDP.
 */
public class RDP extends HttpServlet {

	private static final long serialVersionUID = 1L;

	ManagementVMService mangementVMService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RDP() {
		super();

		mangementVMService = ProxyFactory.create(ManagementVMService.class,
				Configuration.getProperty("managementserver.uri") + "/vm/",
				RESTEasyClientExecutor.get());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		/*
		 * TODO: Use tudUserUniqueID from SSO
		 */
		String userId = "123456";

		// Get request parameters
		Long id = Long.parseLong(request.getParameter("machine"));

		// Get infos from management server
		ManagementVM vm = mangementVMService.getVM(userId, id);

		if (vm.status != VirtualMachineStatus.STARTED) {
			// VM not running, therefore no RDP
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);

			return;
		}

		String filename = Util.generateSlug(vm.name) + ".rdp";

		response.setHeader("Content-type", "application/x-rdp");
		response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");

		PrintWriter out = response.getWriter();
		out.println("full address:s:" + vm.rdpUrl);
		out.println("compression:i:1");
		out.println("displayconnectionbar:i:1");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		doGet(request, response);
	}

}
