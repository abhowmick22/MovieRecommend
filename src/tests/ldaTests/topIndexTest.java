package tests.ldaTests;

import java.util.Arrays;

public class topIndexTest {
	
	public static void main(String[] args){
		
		
		double[] gammaArr = {0.3, 0.5, 0.0, 0.0, 0.4, 0.1, 0.0};
		
		double[] copy = Arrays.copyOf(gammaArr,gammaArr.length);
		Arrays.sort(copy);
		double[] honey = Arrays.copyOfRange(copy,copy.length - 4, copy.length);
        int[] topTopicIndices = new int[4];
        int resultPos = 0;
        for(int i = 0; i < gammaArr.length; i++) {
            double onTrial = gammaArr[i];
            int index = Arrays.binarySearch(honey,onTrial);
            if(index < 0) continue;
            topTopicIndices[resultPos++] = i;
           
        }
        
        for(int i=0; i< topTopicIndices.length; i++)
        	System.out.println(topTopicIndices[i]);
		
	}

}
