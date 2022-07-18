/*Copyright 2010 by INRA SAD-Paysage (Rennes)

Author: Hugues BOUSSARD 
email : hugues.boussard@rennes.inra.fr

This library is a JAVA toolbox made to create and manage dynamic landscapes.

This software is governed by the CeCILL-C license under French law and
abiding by the rules of distribution of free software.  You can  use,
modify and/ or redistribute the software under the terms of the CeCILL-C
license as circulated by CEA, CNRS and INRIA at the following URL
"http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided only
with a limited warranty  and the software's author,  the holder of the
economic rights,  and the successive licensors  have only  limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading,  using,  modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean  that it is complicated to manipulate, and that also
therefore means  that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards their
requirements in conditions enabling the security of their systems and/or
data to be ensured and,  more generally, to use and operate it in the
same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-C license and that you accept its terms.
*/
package fr.inra.sad.bagap.apiland.core.util;

import java.io.File;
import java.util.StringTokenizer;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import fr.inra.sad.bagap.apiland.core.util.AscReader;
import ij.plugin.LUT_Editor;
import ij.plugin.LutLoader;
import ij.process.ImageProcessor;

/**
 * classe de mod�lisation de la visualisation
 * en image via ImageJ
 * @author Hugues Boussard
 */
public class VisuImageJ {
	
	/**
	 * routine de cr�ation
	 * @param file : fichier image
	 */
	public VisuImageJ(String file){
		ImageJ ij = IJ.getInstance();
		if (ij == null || (ij != null && !ij.isShowing())) {
			ij = new ImageJ();
 		}
	
        //IJ.run("Close");
		
        String ext = getExtension(file);
        if(ext.equalsIgnoreCase("asc")){
        	visualizeAsciiGrid(file);
        	IJ.run("Fire");
        }else if(ext.equalsIgnoreCase("tif") || ext.equalsIgnoreCase("tiff")){
        	visualizeTiff(file);
        	IJ.run("Fire");
        }else{
        	//throw new IllegalArgumentException(ext+" not implemented yet");
        	//System.out.println(ext+" not implemented yet");
        }
        
        //LutLoader ll = new LutLoader();
        //ll.run("fire");
        
        //LUT_Editor e = new LUT_Editor();
        //e.run("fire");
	}
	
	private static void visualizeTiff(String file) {
		ImagePlus img = new ImagePlus(file);
		if(img != null){
			img.show();
		}
	}

	private static void visualizeAsciiGrid(String file){
		AscReader tr = new AscReader();
		ImageProcessor ip = tr.open(file);
        if(ip != null){
        	String nom = deleteExtension(getNomCourt(file));
        	ImagePlus imp = new ImagePlus(nom, ip);
        	imp.show();
        }
	}

	private static String getExtension(String file){
		StringTokenizer st = new StringTokenizer(file,".");
		String last = null;
		while(st.hasMoreTokens()){
			last = st.nextToken();
		}
		return last;
	}
	
	/**
	 * supression de l'extension d'un fichier
	 * @param fichier : fichier
	 * @return le nom du fichier sans l'extension
	 */
	private static String deleteExtension(String file){
		String line="";
		StringTokenizer st = new StringTokenizer(file,".");
		String last="";
		while(st.hasMoreTokens()){
			last = last + line;
			line = st.nextToken();
		}
		return last;
	}
	
	/**
	 * r�cup�ration du nom court d'un fichier
	 * @param nom : nom absolu du fichier
	 * @return le nom court
	 */
	private static String getNomCourt(String nom){
		File f = new File(nom);
		return f.getName();
	}
}
