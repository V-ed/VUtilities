package tools;

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

public class FileTools {
	
	private FileTools(){}
	
	public static String copyImageToLocalDirectoryOfProject(String filePath){
		
		Path origin = Paths.get(filePath);
		
		File projectFilePath = FileTools.getExecutablePathDirectoryAsFile();
		
		String projectPath = projectFilePath.getParent();
		
		String fileName = new File(filePath).getName();
		
		createDirsIfNotExists(projectPath);
		
		Path destination = Paths.get(projectPath + "\\" + fileName);
		
		CopyOption[] options = new CopyOption[]
		{
			StandardCopyOption.COPY_ATTRIBUTES
		};
		
		try{
			Files.copy(origin, destination, options);
		}
		catch(IOException e){}
		
		return fileName;
		
	}
	
	public static File getFileFromResources(String path){
		return new File(FileTools.class.getResource(path).getFile());
	}
	
	public static Image getImageFromResources(String imagePath){
		return getImageIconFromResources(imagePath).getImage();
	}
	
	public static ImageIcon getImageIconFromResources(String imagePath){
		return new ImageIcon(
				FileTools.class.getResource("/images/" + imagePath));
	}
	
	public static Image getImageFromProject(String path){
		return getImageIconFromProject(path).getImage();
	}
	
	public static ImageIcon getImageIconFromProject(String path){
		
		String filePath = getExecutablePathDirectory() + "\\" + path;
		
		return getImageIconFromAbsolutePath(filePath);
		
	}
	
	public static ImageIcon getImageIconFromAbsolutePath(String filePath){
		return new ImageIcon(filePath);
	}
	
	public static String getFileNameFromPath(String filePath){
		return new File(filePath).getName();
	}
	
	public static File getExecutablePathDirectoryAsFile(){
		try{
			return new File(FileTools.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath());
		}
		catch(URISyntaxException e){
			return null;
		}
	}
	
	public static String getExecutablePathDirectory(){
		return getExecutablePathDirectoryAsFile().getParent();
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
