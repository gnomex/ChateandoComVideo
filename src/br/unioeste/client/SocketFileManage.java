package br.unioeste.client;

import java.io.IOException;
import java.util.List;

import br.unioeste.common.handler.UCHandlerArchiveManager;
import br.unioeste.common.handler.UCHandlerSolicitationManager;
import br.unioeste.messenger.FileListener;
import br.unioeste.messenger.ManageFile;
import br.unioeste.util.Archive;

public class SocketFileManage implements ManageFile{
	
	@Override
	public void receivedFile(FileListener filelistener , String fileName , String path) {
	
		 UCHandlerArchiveManager uc = new UCHandlerArchiveManager();
		 uc.receiveFile(fileName, path );
		 
		 filelistener.fileTransferStatus("File: " + fileName + " Downloading, save on " + path);
		
		
	}

	@Override
	public void listAvaibleFiles(FileListener filelistener) {

		UCHandlerSolicitationManager uCHandlerSolicitationManager = new UCHandlerSolicitationManager();
		List<Archive> list = null;

		try {
			list = uCHandlerSolicitationManager.listArchives();
			
			if(!list.isEmpty()){
				filelistener.fileListAvaibleReceived(list);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendFile(FileListener filelistener, String archivePath) {
		 try {
           UCHandlerArchiveManager uc =  new UCHandlerArchiveManager();
           uc.sendFile(archivePath);
           
           filelistener.fileTransferStatus("File: " + archivePath + " -> upload Begin");
           
         } catch (IOException ex) {
             ex.printStackTrace();
         }
		
	}
	

}
