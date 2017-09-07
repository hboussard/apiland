package fr.inra.sad.bagap.apiland.core.util;

import java.io.IOException;
import java.io.Serializable;

import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;

public class TransitionMatrixFactory {

	public static <O extends Serializable> TransitionMatrix<O> create(String f, Class<?> c){
		TransitionMatrix<O> tMat = null;
		try{
			CsvReader cr = new CsvReader(f);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			TransitionMatrixBuilder<O> builder = new TransitionMatrixBuilder<O>();
			
			while(cr.readRecord()){
				for(String in : cr.getHeaders()){
					if(!in.equalsIgnoreCase("out-in")){
						builder.setTransitionRate((O)getFromString(in, c), (O)getFromString(cr.get("out-in"), c), Double.parseDouble(cr.get(in)));
					}
				}
			}
			
			tMat = builder.build();
			
			cr.close();
		}catch(IOException ex){
			ex.printStackTrace();
		} catch (FinalizedException e) {
			e.printStackTrace();
		} catch (CatastrophicException e) {
			e.printStackTrace();
		}
		
		return tMat;
	}
	
	public static Serializable getFromString(String s, Class<?> c){
		if(c.equals(Integer.class)){
			return new Integer(s);
		}
		throw new IllegalArgumentException();
	}
	
}
