package xsystem.layers;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import xsystem.support.Config;
import xsystem.support.Utils;
import xsystem.support.Wrapper;
import xsystem.enums.XClass;

public class Token {

//Token Layer of the XStructure

	public ArrayList<Symbol> symbols;

	public Boolean doneAdding;

	public ArrayList<Double> history;

	public double stdDev;

	public ArrayList<Double> archive;

	private final boolean tryToStop = Config.tts;

	public ArrayList<String> tokenStringGenerator;

	public double scoreSoFar;

	public ArrayList<XClass> representation;

	public Token(){
		this.symbols = new ArrayList<>();
		this.doneAdding = false;
		this.history = new ArrayList<>();
		this.stdDev = 100.0;
		this.archive = new ArrayList<>();
		this.tokenStringGenerator = tokenStringGenerator(symbols);
		this.scoreSoFar = ((double)archive.stream().mapToDouble(a -> a).sum())/(archive.size());
		this.representation = representationFunc(symbols);
	}
	
	public Token(ArrayList<Symbol> symbols, Boolean doneAdding, ArrayList<Double> history, double stdDev, ArrayList<Double> archive) {
		this.symbols = symbols;
		this.stdDev = stdDev;
		this.tokenStringGenerator = tokenStringGenerator(symbols);
		this.scoreSoFar = ((double)archive.stream().mapToDouble(a -> a).sum())/(archive.size());
		this.representation = representationFunc(symbols);

		if(tryToStop){
			this.doneAdding = doneAdding;
			this.history = history;
			this.archive = archive;
		}
		else {
			this.doneAdding = false;
			this.history = new ArrayList<>();
			this.archive = new ArrayList<>();
		}
	}

	@JsonCreator
	public Token(@JsonProperty("symbols") ArrayList<Symbol> symbols, @JsonProperty("doneAdding") Boolean doneAdding, 
	@JsonProperty("history") ArrayList<Double> history,@JsonProperty("stdDev") double stdDev,
	@JsonProperty("archive") ArrayList<Double> archive, @JsonProperty("tokenStringGenerator") ArrayList<String> tokenStringGenerator,
	@JsonProperty("scoreSoFar") double scoreSoFar, @JsonProperty("representation") ArrayList<XClass> representation)
	{
        super();
        this.symbols = symbols;
		this.doneAdding = doneAdding;
		this.history = history;
		this.stdDev = stdDev;
		this.archive = archive;
		this.tokenStringGenerator = tokenStringGenerator;
		this.scoreSoFar = scoreSoFar;
		this.representation = representation;
    }
	
	// Getters and Setters

	public ArrayList<Symbol> getSymbols() {
		return symbols;
	}

	public void setSymbols(ArrayList<Symbol> symbols) {
		this.symbols = symbols;
	}

	public Boolean getDoneAdding() {
		return doneAdding;
	}

	public void setDoneAdding(Boolean doneAdding) {
		this.doneAdding = doneAdding;
	}

	public ArrayList<Double> getHistory() {
		return history;
	}

	public void setHistory(ArrayList<Double> history) {
		this.history = history;
	}

	public double getStdDev() {
		return stdDev;
	}

	public void setStdDev(double stdDev) {
		this.stdDev = stdDev;
	}

	public ArrayList<Double> getArchive() {
		return archive;
	}

	public void setArchive(ArrayList<Double> archive) {
		this.archive = archive;
	}

	public ArrayList<String> getTokenStringGenerator() {
		return tokenStringGenerator;
	}

	public void setTokenStringGenerator(ArrayList<String> tokenStringGenerator) {
		this.tokenStringGenerator = tokenStringGenerator;
	}

	public double getScoreSoFar() {
		return scoreSoFar;
	}

	public void setScoreSoFar(double scoreSoFar) {
		this.scoreSoFar = scoreSoFar;
	}

	public ArrayList<XClass> getRepresentation() {
		return representation;
	}

	public void setRepresentation(ArrayList<XClass> representation) {
		this.representation = representation;
	}

	public boolean isTryToStop() {
		return tryToStop;
	}

	private ArrayList<String> tokenStringGenerator(ArrayList<Symbol> symbols){
		int len = symbols.size();
		ArrayList<ArrayList<Wrapper>> lst = new ArrayList<>();
		for(int i=0; i<len; i++){
			ArrayList<Wrapper> strList = new ArrayList<>();
			ArrayList<String> strlr = symbols.get(i).symbolStringGenerator;
			for(String str : strlr){
				String res = str + "-" + String.valueOf(i);
				strList.add(new Wrapper(res));
			}
			lst.add(strList);
		}

		ArrayList<Wrapper> res = Utils.mergeStreams(lst);
		ArrayList<String> result = new ArrayList<>();

		for(Wrapper w : res){
			result.add(w.getString());
		}

		return result;
	}

	private ArrayList<XClass> representationFunc(ArrayList<Symbol> symbolList){
		ArrayList<XClass> res = new ArrayList<>();

		if(symbolList.isEmpty())
			return res;
		else{
			for(Symbol symbol : symbolList){
				res.add(symbol.representation);
			}
			return res;
		}
	}

	public Token learnToken(String token){
		if(doneAdding)
			return this;
		else{
			double tokenScore = 0;
			
			for(int i=0; i<token.length() && i<symbols.size();i++){
				tokenScore += symbols.get(i).scoreChar(token.charAt(i));
			}

			ArrayList<Symbol> _symbolStructs = new ArrayList<>();

			for(int i=0; i<token.length(); i++){
				if(i>=symbols.size())
					_symbolStructs.add( new Symbol(token.charAt(i)) );
				else 
					_symbolStructs.add(symbols.get(i).addChar(token.charAt(i)));
			}

			ArrayList<Double> _history = history;
			_history.add(tokenScore);

			double _stdDev;
			DescriptiveStatistics stats = new DescriptiveStatistics();
			for(double d : _history) 
				stats.addValue(d);

			if(_history.size() % Config.inc == 0) 
				_stdDev = stats.getStandardDeviation();
			else 
				_stdDev = stdDev;

			Boolean _doneAdding = Config.neededSampleSize(_stdDev) < _history.size();

			ArrayList<Double> _archive = new ArrayList<>();
			if(_doneAdding){
				_archive.addAll(archive);
				_archive.addAll(_history);
			}
			else{
				_archive.addAll(archive);
			}

			return new Token(_symbolStructs, _doneAdding, _history, _stdDev, _archive);
		}
	}

	public Token reopened(){
		ArrayList<Double> _archive = new ArrayList<>();
		_archive.addAll(archive);
		_archive.addAll(history);

		return new Token(symbols, false, new ArrayList<>(), stdDev, _archive);
	}
	
	public double scoreToken(String token){
		double score = 0;

		for(int i = 0; i<token.length() && i<symbols.size(); i++){
			score += symbols.get(i).scoreChar(token.charAt(i));
		}
		return score;
	}

	public String randomToken(){
		String res = "";
		for(XClass x : representation){
			res += x.randomRep();
		}
		return res;
	}

	public String toString(){
		String res = "";
		for(XClass x : representation){
			res += x.toString();
		}
		return res;
	}
}
