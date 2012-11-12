package br.unioeste.common.handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import br.unioeste.common.Solicitation;
import br.unioeste.server.file.transmission.UDPComunication;

public class DiscoveryHandler implements Runnable {

	Solicitation solicitation;

	/**
	 * @param object
	 */
	public DiscoveryHandler(Object object) {
		this.solicitation = (Solicitation) object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		if (solicitation.getCode() == Solicitation.DISCOVER) {

			try {

				UDPComunication com = new UDPComunication();
				Solicitation reply = new Solicitation();

				reply.setAddress(InetAddress.getLocalHost().getHostAddress());
				reply.setCode(Solicitation.DISCOVER_REPLY);

				com.sendObject(solicitation.getAddress(),
						solicitation.getPort(), reply);

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
