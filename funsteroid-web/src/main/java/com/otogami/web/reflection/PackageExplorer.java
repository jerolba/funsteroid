package com.otogami.web.reflection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class PackageExplorer {

	public static Set<Class<?>> getClasses(String packageName) throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return getClasses(loader, packageName);
	}

	static Set<Class<?>> getClasses(ClassLoader loader, String packageName) throws Exception {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = loader.getResources(path);
		if (resources != null) {
			while (resources.hasMoreElements()) {
				String filePath = resources.nextElement().getFile();
				// WINDOWS HACK
				if (filePath.indexOf("%20") > 0){
					filePath = filePath.replaceAll("%20", " ");
				}
				if (filePath != null) {
					if ((filePath.indexOf("!") > 0)
							& (filePath.indexOf(".jar") > 0)) {
						String jarPath = filePath.substring(0,
								filePath.indexOf("!")).substring(
								filePath.indexOf(":") + 1);
						// WINDOWS HACK
						if (jarPath.indexOf(":") >= 0){
							jarPath = jarPath.substring(1);
						}
						classes.addAll(getFromJARFile(jarPath, path));
					} else {
						classes.addAll(getFromDirectory(new File(filePath),packageName));
					}
				}
			}
		}
		return classes;
	}

	static Set<Class<?>> getFromDirectory(File directory, String packageName) throws Exception {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
				if (file.isDirectory()){
					classes.addAll(getFromDirectory(file, packageName+"."+file.getName()));
				}else if (file.getName().endsWith(".class")) {
					String name = packageName + '.'
							+ stripFilenameExtension(file.getName());
					Class<?> clazz = Class.forName(name);
					classes.add(clazz);
				}
			}
		}
		return classes;
	}

	static Set<Class<?>> getFromJARFile(String jar, String packageName) throws Exception, IOException {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		JarInputStream jarFile = new JarInputStream(new FileInputStream(jar));
		JarEntry jarEntry;
		do {
			jarEntry = jarFile.getNextJarEntry();
			if (jarEntry != null) {
				String className = jarEntry.getName();
				if (className.endsWith(".class")) {
					className = stripFilenameExtension(className);
					if (className.startsWith(packageName)){
						classes.add(Class.forName(className.replace('/', '.')));
					}
				}
			}
		} while (jarEntry != null);
		return classes;
	}
	
	static String stripFilenameExtension(String fileName){
		return fileName.substring(0,fileName.lastIndexOf("."));
	}
}
