package edu.sharif.ce.nipl.dmoo.business;

import com.fuzzylite.Engine;
import com.fuzzylite.FuzzyLite;
import com.fuzzylite.Op;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Triangle;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;

public class Coeffs {

	public static int calSMP(int last_change , int current_iter , int max_iter){

		double result = 5;
		double t = 0;
		double et = 0;
		double kasret=0;
		double MRmin = 4;
		double MRmax = 6;
		double temp=0;
		if(last_change != 0)
			//result -= 2 * ((double)(current_iter - last_change) / (max_iter));
			t = 100 * ((double) 1 / 5) * ((double)(current_iter - last_change) / 25);
		et = Math.pow(Math.E, t);
		kasret = (double)( 1 / et);
		temp = (double)(1) / (double)( 1 - Math.log10(kasret));
		result = MRmin - (( MRmin - MRmax) * temp);


		//System.out.println((int)result);
		return (int) result;
	}

	
	public static double[] calSRDCDC(double prevSRDValue , double prevCDCValue, double hvrValue, double changeValue){
		Engine engine = new Engine();
		engine.setName("simple-SRD+CDC");

		InputVariable prevSRD = new InputVariable();
		prevSRD.setName("PREVSRD");
		prevSRD.setRange(0.10, 0.30);
		prevSRD.addTerm(new Triangle("LOW", 0.10, 0.15, 0.20));
		prevSRD.addTerm(new Triangle("MEDIUM", 0.18, 0.22, 0.25));
		prevSRD.addTerm(new Triangle("HIGH", 0.23, 0.28, 0.30));
		engine.addInputVariable(prevSRD);
		
		InputVariable prevCDC = new InputVariable();
		prevCDC.setName("PREVCDC");
		prevCDC.setRange(0.45, 0.75);
		prevCDC.addTerm(new Triangle("LOW", 0.45, 0.50, 0.55));
		prevCDC.addTerm(new Triangle("MEDIUM", 0.50, 0.60, 0.65));
		prevCDC.addTerm(new Triangle("HIGH", 0.60, 0.70, 0.75));
		engine.addInputVariable(prevCDC);

		InputVariable HVR = new InputVariable();
		HVR.setName("HVR");
		HVR.setRange(0.5, 1);
		HVR.addTerm(new Triangle("LOW", 0.50, 0.90, 0.970));
		HVR.addTerm(new Triangle("MEDIUM", 0.965, 0.985, 0.995));
		HVR.addTerm(new Triangle("HIGH", 0.990, 0.995, 1));
		engine.addInputVariable(HVR);
		
		InputVariable changeVal = new InputVariable();
		changeVal.setName("ChangeVal");
		changeVal.setRange(0, 0.1);
		changeVal.addTerm(new Triangle("LOW", 0, 0.05, 0.09));
		changeVal.addTerm(new Triangle("MEDIUM", 0.085, 0.1, 0.15));
		changeVal.addTerm(new Triangle("HIGH", 0.14, 0.2, 0.3));
		engine.addInputVariable(changeVal);


		OutputVariable nextSRD = new OutputVariable();
		nextSRD.setName("NEXTSRD");
		nextSRD.setRange(0.10, 0.30);
		nextSRD.addTerm(new Triangle("LOW", 0.10, 0.15, 0.20));
		nextSRD.addTerm(new Triangle("MEDIUM", 0.18, 0.22, 0.25));
		nextSRD.addTerm(new Triangle("HIGH", 0.23, 0.28, 0.30));
		nextSRD.setDefaultValue(0.2);
		engine.addOutputVariable(nextSRD);
		
		
		OutputVariable nextCDC = new OutputVariable();
		nextCDC.setName("NEXTCDC");
		nextCDC.setRange(0.45, 0.75);
		nextCDC.addTerm(new Triangle("LOW", 0.45, 0.50, 0.55));
		nextCDC.addTerm(new Triangle("MEDIUM", 0.54, 0.60, 0.65));
		nextCDC.addTerm(new Triangle("HIGH", 0.64, 0.70, 0.75));
		nextCDC.setDefaultValue(0.6);
		engine.addOutputVariable(nextCDC);

		RuleBlock ruleBlock = new RuleBlock();
		
		
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is LOW or HVR is MEDIUM and PREVSRD is LOW and PREVCDC is LOW then NEXTSRD is not LOW and NEXTCDC is not LOW", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is LOW or HVR is MEDIUM and PREVSRD is LOW and PREVCDC is MEDIUM then NEXTSRD is not LOW and NEXTCDC is not MEDIUM", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is LOW or HVR is MEDIUM and PREVSRD is LOW and PREVCDC is HIGH then NEXTSRD is not LOW and NEXTCDC is not HIGH", engine));
		
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is LOW or HVR is MEDIUM and PREVSRD is MEDIUM and PREVCDC is LOW then NEXTSRD is not MEDIUM and NEXTCDC is not LOW", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is LOW or HVR is MEDIUM and PREVSRD is MEDIUM and PREVCDC is MEDIUM then NEXTSRD is not MEDIUM and NEXTCDC is not MEDIUM", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is LOW or HVR is MEDIUM and PREVSRD is MEDIUM and PREVCDC is HIGH then NEXTSRD is not MEDIUM and NEXTCDC is not HIGH", engine));
		
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is LOW or HVR is MEDIUM and PREVSRD is HIGH and PREVCDC is LOW then NEXTSRD is not HIGH and NEXTCDC is not LOW", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is LOW or HVR is MEDIUM and PREVSRD is HIGH and PREVCDC is MEDIUM then NEXTSRD is not HIGH and NEXTCDC is not MEDIUM", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is LOW or HVR is MEDIUM and PREVSRD is HIGH and PREVCDC is HIGH then NEXTSRD is not HIGH and NEXTCDC is not HIGH", engine));
		
		
		
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is HIGH and PREVSRD is LOW and PREVCDC is LOW then NEXTSRD is LOW and NEXTCDC is LOW", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is HIGH and PREVSRD is LOW and PREVCDC is MEDIUM then NEXTSRD is LOW and NEXTCDC is MEDIUM", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is HIGH and PREVSRD is LOW and PREVCDC is HIGH then NEXTSRD is LOW and NEXTCDC is HIGH", engine));
		
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is HIGH and PREVSRD is MEDIUM and PREVCDC is LOW then NEXTSRD is MEDIUM and NEXTCDC is LOW", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is HIGH and PREVSRD is MEDIUM and PREVCDC is MEDIUM then NEXTSRD is MEDIUM and NEXTCDC is MEDIUM", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is HIGH and PREVSRD is MEDIUM and PREVCDC is HIGH then NEXTSRD is MEDIUM and NEXTCDC is HIGH", engine));
		
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is HIGH and PREVSRD is HIGH and PREVCDC is LOW then NEXTSRD is HIGH and NEXTCDC is LOW", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is HIGH and PREVSRD is HIGH and PREVCDC is MEDIUM then NEXTSRD is HIGH and NEXTCDC is MEDIUM", engine));
		ruleBlock.addRule(Rule.parse("if ChangeVal is LOW or ChangeVal is MEDIUM and HVR is HIGH and PREVSRD is HIGH and PREVCDC is HIGH then NEXTSRD is HIGH and NEXTCDC is HIGH", engine));
		
		ruleBlock.addRule(Rule.parse("if ChangeVal is HIGH then NEXTSRD is LOW and NEXTCDC is HIGH", engine));
		
		
		engine.addRuleBlock(ruleBlock);

		engine.configure("Minimum", "Maximum", "Minimum", "Maximum", "Centroid");

		StringBuilder status = new StringBuilder();
		if (!engine.isReady(status)) {
			throw new RuntimeException("Engine not ready. "
					+ "The following errors were encountered:\n" + status.toString());
		}


		//Define the inputs
		prevSRD.setInputValue(prevSRDValue);
		prevCDC.setInputValue(prevCDCValue);		
		HVR.setInputValue(hvrValue);
		changeVal.setInputValue(changeValue);
		engine.process();

		double[] result = {nextSRD.getOutputValue(), nextCDC.getOutputValue()};
	    return result;

		//return nextSRD.getOutputValue();
		

	}

	// alpha is coefficient of before change
	public static double calAlpha(double perfVar , double changeVar , double prevAlphaVar){
		Engine engine = new Engine();
		engine.setName("simple-Alpha");
		
		InputVariable prevAlpha = new InputVariable();
		prevAlpha.setName("PREVALPHA");
		prevAlpha.setRange(0.0, 0.5);
		prevAlpha.addTerm(new Triangle("LOW", 0.0, 0.15, 0.3));
		prevAlpha.addTerm(new Triangle("HIGH", 0.20, 0.35, 0.5));
		engine.addInputVariable(prevAlpha);

		InputVariable HVR = new InputVariable();
		HVR.setName("HVR");
		HVR.setRange(0.5, 1);
		HVR.addTerm(new Triangle("LOW", 0.50, 0.90, 0.970));
		//HVR.addTerm(new Triangle("MEDIUM", 0.965, 0.985, 0.995));
		HVR.addTerm(new Triangle("HIGH", 0.960, 0.98, 1));
		engine.addInputVariable(HVR);
		
		InputVariable changeVal = new InputVariable();
		changeVal.setName("ChangeVal");
		changeVal.setRange(0, 0.3);
		changeVal.addTerm(new Triangle("LOW", 0, 0.05, 0.18));
		//changeVal.addTerm(new Triangle("MEDIUM", 0.085, 0.1, 0.15));
		changeVal.addTerm(new Triangle("HIGH", 0.15, 0.25, 0.3));
		engine.addInputVariable(changeVal);

		
		OutputVariable nextAlpha = new OutputVariable();
		nextAlpha.setName("NEXTALPHA");
		nextAlpha.setRange(0.0, 0.50);
		nextAlpha.addTerm(new Triangle("LOW", 0.0, 0.15, 0.3));
		nextAlpha.addTerm(new Triangle("HIGH", 0.20, 0.35, 0.5));
		nextAlpha.setDefaultValue(0.2);
		engine.addOutputVariable(nextAlpha);

		RuleBlock ruleBlock = new RuleBlock();
		
		ruleBlock.addRule(Rule.parse("if HVR is LOW then NEXTALPHA is LOW", engine));
		
		ruleBlock.addRule(Rule.parse("if HVR is HIGH and ChangeVal is LOW and PREVALPHA is LOW then NEXTALPHA is LOW", engine));
		ruleBlock.addRule(Rule.parse("if HVR is HIGH and ChangeVal is LOW and PREVALPHA is HIGH then NEXTALPHA is HIGH", engine));
		
		ruleBlock.addRule(Rule.parse("if HVR is HIGH and ChangeVal is HIGH then NEXTALPHA is HIGH", engine));
		
		engine.addRuleBlock(ruleBlock);

		engine.configure("Minimum", "Maximum", "Minimum", "Maximum", "Centroid");

		StringBuilder status = new StringBuilder();
		if (!engine.isReady(status)) {
			throw new RuntimeException("Engine not ready. "
					+ "The following errors were encountered:\n" + status.toString());
		}


		//Define the inputs
		prevAlpha.setInputValue(prevAlphaVar);
		HVR.setInputValue(perfVar);
		changeVal.setInputValue(changeVar);
		engine.process();
		
		double result = nextAlpha.getOutputValue();
	    return result;

		//return nextSRD.getOutputValue();
		

	}
	public static double calMR(double MR , int last_change , int current_iter , int max_iter){
		double result = MR;
		double t = 0;
		double et = 0;
		double kasret=0;
		double MRmin = 0.94;
		double MRmax = 0.99;
		double temp=0;
		if(last_change != 0)
			//result -= 2 * ((double)(current_iter - last_change) / (max_iter));
			t = 100 * ((double) 1 / 5) * ((double)(current_iter - last_change) / 25);
		et = Math.pow(Math.E, t);
		kasret = (double)( 1 / et);
		temp = (double)(1) / (double)( 1 - Math.log10(kasret));
		result = MRmin - (( MRmin - MRmax) * temp);


		//System.out.println(result);
		return result;
		//return 0.98;
	}

	public
	static double calC(int last_change , int current_iter , int max_iter){
		//return C - (double)(max_iter - iter) / (2 * max_iter);
		double result = 2;
		double t = 0;
		double et = 0;
		double kasret=0;
		double MRmin = 2;
		double MRmax = 1.5;
		double temp=0;
		if(last_change != 0)
			//result -= 2 * ((double)(current_iter - last_change) / (max_iter));
			t = 100 * ((double) 1 / 5) * ((double)(current_iter - last_change) / 25);
		et = Math.pow(Math.E, t);
		kasret = (double)( 1 / et);
		temp = (double)(1) / (double)( 1 - Math.log10(kasret));
		result = MRmin - (( MRmin - MRmax) * temp);


		//System.out.println(result);
		return result;
		//return 0.98;
	}

	public static double calW(int last_change , int current_iter , int max_iter){
		//return W + (double)(max_iter - iter) / (2 * max_iter);
		double result = 0.7;
		double t = 0;
		double et = 0;
		double kasret=0;
		double MRmin = 0.3;
		double MRmax = 0.8;
		double temp=0;
		if(last_change != 0)
			//result -= 2 * ((double)(current_iter - last_change) / (max_iter));
			t = 100 * ((double) 1 / 5) * ((double)(current_iter - last_change) / 25);
		et = Math.pow(Math.E, t);
		kasret = (double)( 1 / et);
		temp = (double)(1) / (double)( 1 - Math.log10(kasret));
		result = MRmin - (( MRmin - MRmax) * temp);


		//System.out.println(result);
		return result;
		//return 0.98;
	}


}
