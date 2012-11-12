package br.unioeste.messenger;


public interface ManageFile {
	
	public void sendFile( FileListener filelistener , String archivePath);
	
	public void receivedFile( FileListener filelistener , String fileName , String path);
	
	public void listAvaibleFiles( FileListener filelistener );


}
