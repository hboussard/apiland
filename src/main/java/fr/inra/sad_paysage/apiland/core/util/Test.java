package fr.inra.sad_paysage.apiland.core.util;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		TransitionMatrixBuilder<Integer> builder = new TransitionMatrixBuilder<Integer>();
		builder.setTransitionRate(1, 1, 10);
		builder.setTransitionRate(1, 2, 60);
		builder.setTransitionRate(1, 3, 30);
		builder.setTransitionRate(2, 1, 40);
		builder.setTransitionRate(2, 2, 10);
		builder.setTransitionRate(2, 3, 50);
		builder.setTransitionRate(3, 1, 40);
		builder.setTransitionRate(3, 2, 40);
		builder.setTransitionRate(3, 3, 20);
		TransitionMatrix<Integer> tMat = builder.build();
		
		int total = 1000;
		List<Integer> l = new ArrayList<Integer>(total);
		for(int i=0; i<total; i++){
			l.add(tMat.getTransition(1));
		}
		
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		for(int v : l){
			if(v == 1){
				count1++;
			}else if(v == 2){
				count2++;
			}else{
				count3++;
			}
		}
		
		System.out.println("pourcentage de '1' --> "+count1*100.0/total+"% sur 10% estimé.");
		System.out.println("pourcentage de '2' --> "+count2*100.0/total+"% sur 60% estimé.");
		System.out.println("pourcentage de '3' --> "+count3*100.0/total+"% sur 30% estimé.");
	}

}
