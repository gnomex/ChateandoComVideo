package br.unioeste.messenger;

import java.util.List;

import br.unioeste.util.Archive;

public interface FileListener {
	
	//File that has received
	public void fileReceived( Archive archive);
	//File list avaible dor download
	public void fileListAvaibleReceived( List<Archive> archives);
	
	public void fileTransferStatus( String status);

}
