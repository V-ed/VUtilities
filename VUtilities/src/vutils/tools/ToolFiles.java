package vutils.tools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

// TODO Javadoc

public class ToolFiles {
	
	private ToolFiles(){}
	
	public static String copyImageToLocalDirectoryOfProject(String filePath,
			String... folders){
		
		Path origin = Paths.get(filePath);
		
		String projectPath = getExecutablePathDirectory();
		
		String fileName = new File(filePath).getName();
		
		if(folders != null)
			for(int i = 0; i < folders.length; i++)
				projectPath += "\\" + folders[i];
		
		createDirsIfNotExists(projectPath);
		
		Path destination = Paths.get(projectPath + "\\" + fileName);
		
		CopyOption[] options = new CopyOption[]
		{
			StandardCopyOption.COPY_ATTRIBUTES
		};
		
		String imageInProjectPath = null;
		
		try{
			
			Files.copy(origin, destination, options);
			
			if(folders != null)
				for(int i = 0; i < folders.length; i++)
					imageInProjectPath += folders[i] + "\\";
			
			imageInProjectPath += fileName;
			
		}
		catch(IOException e){}
		
		return imageInProjectPath;
		
	}
	
	public static String copyImageToLocalDirectoryOfProject(String filePath){
		return copyImageToLocalDirectoryOfProject(filePath, new String[] {});
	}
	
	public static File getFileFromResources(String path){
		return new File(ToolFiles.class.getResource(path).getFile());
	}
	
	public static Image getImageFromResources(String imagePath){
		return getImageIconFromResources(imagePath).getImage();
	}
	
	public static ImageIcon getImageIconFromResources(String imagePath){
		return new ImageIcon(ToolFiles.class.getResource(imagePath));
	}
	
	public static Image getImageFromProject(String path){
		return getImageIconFromProject(path).getImage();
	}
	
	public static ImageIcon getImageIconFromProject(String path){
		
		return getImageIconFromAbsolutePath(getFileFromLocalDirectoryOfProject(
				path).getAbsolutePath());
		
	}
	
	public static ImageIcon getImageIconFromAbsolutePath(String filePath){
		return new ImageIcon(filePath);
	}
	
	public static String getFileNameFromPath(String filePath){
		return new File(filePath).getName();
	}
	
	public static File getExecutablePathDirectoryAsFile(){
		try{
			return new File(ToolFiles.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath());
		}
		catch(URISyntaxException e){
			return null;
		}
	}
	
	public static String getExecutablePathDirectory(){
		return getExecutablePathDirectoryAsFile().getParentFile().getParent();
	}
	
	private static void createDirsIfNotExists(String directoryPath){
		
		File file = new File(directoryPath);
		
		if(!file.isDirectory()){
			file.mkdirs();
		}
		
	}
	
	public static File getFileFromLocalDirectoryOfProject(String path){
		
		return new File(getExecutablePathDirectory() + "\\" + path);
		
	}
	
	public static BufferedImage getBufferedImageFromProject(String path){
		try{
			return ImageIO.read(getFileFromLocalDirectoryOfProject(path));
		}
		catch(IOException e){
			return null;
		}
	}
	
}
