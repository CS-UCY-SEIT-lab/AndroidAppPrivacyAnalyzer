package privacyanalyzer.backend.data.entity;

import javax.persistence.Entity;

@Entity
public class Variables extends MyAbstractEntity {

	private String name;
	

	
	private double declaredAndUsedDangerousPermissionScore;
	private double declaredAndUsedSignatureSystemPermissionScore;
	private double declaredAndNotUsedDangerousPermissionScore;
	private double declaredAndNotUsedSignatureSystemPermissionScore;
	private double notDeclaredButUsedDangerousPermissionScore;
	private double notDeclaredButUsedSignatureSystemPermissionScore;
	private double notDeclaredButUsedNormalPermissionScore;
	private double libraryDangerousPermissionScore;
	private double librarySignatureSystemPermissionScore;
	
	private double adbScore;
	private double debuggableScore;
	
	private double malwareScore;
	
	private double maximumRiskScore;
	
	private double greenRisk;
	private double yellowRisk;
	private double orangRisk;
	private double redRisk;
	
	private double virusTotalScore;
	private double virusTotalThreshold;
	/**
	 * 
	 */
	public Variables() {
	}
	
	/**
	 * @param declaredAndUsedDangerousPermissionScore
	 * @param declaredAndUsedSignatureSystemPermissionScore
	 * @param declaredAndNotUsedDangerousPermissionScore
	 * @param declaredAndNotUsedSignatureSystemPermissionScore
	 * @param notDeclaredButUsedDangerousPermissionScore
	 * @param notDeclaredButUsedSignatureSystemPermissionScore
	 * @param notDeclaredButUsedNormalPermissionScore
	 * @param libraryDangerousPermissionScore
	 * @param librarySignatureSystemPermissionScore
	 * @param adbScore
	 * @param debuggableScore
	 * @param malwareScore
	 * @param maximumRiskScore
	 * @param greenRisk
	 * @param yellowRisk
	 * @param orangRisk
	 * @param redRisk
	 */
	public Variables(String name,double declaredAndUsedDangerousPermissionScore,
			double declaredAndUsedSignatureSystemPermissionScore, double declaredAndNotUsedDangerousPermissionScore,
			double declaredAndNotUsedSignatureSystemPermissionScore, double notDeclaredButUsedDangerousPermissionScore,
			double notDeclaredButUsedSignatureSystemPermissionScore, double notDeclaredButUsedNormalPermissionScore,
			double libraryDangerousPermissionScore, double librarySignatureSystemPermissionScore, double adbScore,
			double debuggableScore, double malwareScore, double maximumRiskScore, double greenRisk, double yellowRisk,
			double orangRisk, double redRisk) {
		this.name=name;
		this.declaredAndUsedDangerousPermissionScore = declaredAndUsedDangerousPermissionScore;
		this.declaredAndUsedSignatureSystemPermissionScore = declaredAndUsedSignatureSystemPermissionScore;
		this.declaredAndNotUsedDangerousPermissionScore = declaredAndNotUsedDangerousPermissionScore;
		this.declaredAndNotUsedSignatureSystemPermissionScore = declaredAndNotUsedSignatureSystemPermissionScore;
		this.notDeclaredButUsedDangerousPermissionScore = notDeclaredButUsedDangerousPermissionScore;
		this.notDeclaredButUsedSignatureSystemPermissionScore = notDeclaredButUsedSignatureSystemPermissionScore;
		this.notDeclaredButUsedNormalPermissionScore = notDeclaredButUsedNormalPermissionScore;
		this.libraryDangerousPermissionScore = libraryDangerousPermissionScore;
		this.librarySignatureSystemPermissionScore = librarySignatureSystemPermissionScore;
		this.adbScore = adbScore;
		this.debuggableScore = debuggableScore;
		this.malwareScore = malwareScore;
		this.maximumRiskScore = maximumRiskScore;
		this.greenRisk = greenRisk;
		this.yellowRisk = yellowRisk;
		this.orangRisk = orangRisk;
		this.redRisk = redRisk;
	}
	public double getDeclaredAndUsedDangerousPermissionScore() {
		return declaredAndUsedDangerousPermissionScore;
	}
	public void setDeclaredAndUsedDangerousPermissionScore(double declaredAndUsedDangerousPermissionScore) {
		this.declaredAndUsedDangerousPermissionScore = declaredAndUsedDangerousPermissionScore;
	}
	public double getDeclaredAndUsedSignatureSystemPermissionScore() {
		return declaredAndUsedSignatureSystemPermissionScore;
	}
	public void setDeclaredAndUsedSignatureSystemPermissionScore(double declaredAndUsedSignatureSystemPermissionScore) {
		this.declaredAndUsedSignatureSystemPermissionScore = declaredAndUsedSignatureSystemPermissionScore;
	}
	public double getDeclaredAndNotUsedDangerousPermissionScore() {
		return declaredAndNotUsedDangerousPermissionScore;
	}
	public void setDeclaredAndNotUsedDangerousPermissionScore(double declaredAndNotUsedDangerousPermissionScore) {
		this.declaredAndNotUsedDangerousPermissionScore = declaredAndNotUsedDangerousPermissionScore;
	}
	public double getDeclaredAndNotUsedSignatureSystemPermissionScore() {
		return declaredAndNotUsedSignatureSystemPermissionScore;
	}
	public void setDeclaredAndNotUsedSignatureSystemPermissionScore(
			double declaredAndNotUsedSignatureSystemPermissionScore) {
		this.declaredAndNotUsedSignatureSystemPermissionScore = declaredAndNotUsedSignatureSystemPermissionScore;
	}
	public double getNotDeclaredButUsedDangerousPermissionScore() {
		return notDeclaredButUsedDangerousPermissionScore;
	}
	public void setNotDeclaredButUsedDangerousPermissionScore(double notDeclaredButUsedDangerousPermissionScore) {
		this.notDeclaredButUsedDangerousPermissionScore = notDeclaredButUsedDangerousPermissionScore;
	}
	public double getNotDeclaredButUsedSignatureSystemPermissionScore() {
		return notDeclaredButUsedSignatureSystemPermissionScore;
	}
	public void setNotDeclaredButUsedSignatureSystemPermissionScore(
			double notDeclaredButUsedSignatureSystemPermissionScore) {
		this.notDeclaredButUsedSignatureSystemPermissionScore = notDeclaredButUsedSignatureSystemPermissionScore;
	}
	public double getNotDeclaredButUsedNormalPermissionScore() {
		return notDeclaredButUsedNormalPermissionScore;
	}
	public void setNotDeclaredButUsedNormalPermissionScore(double notDeclaredButUsedNormalPermissionScore) {
		this.notDeclaredButUsedNormalPermissionScore = notDeclaredButUsedNormalPermissionScore;
	}
	public double getLibraryDangerousPermissionScore() {
		return libraryDangerousPermissionScore;
	}
	public void setLibraryDangerousPermissionScore(double libraryDangerousPermissionScore) {
		this.libraryDangerousPermissionScore = libraryDangerousPermissionScore;
	}
	public double getLibrarySignatureSystemPermissionScore() {
		return librarySignatureSystemPermissionScore;
	}
	public void setLibrarySignatureSystemPermissionScore(double librarySignatureSystemPermissionScore) {
		this.librarySignatureSystemPermissionScore = librarySignatureSystemPermissionScore;
	}
	public double getAdbScore() {
		return adbScore;
	}
	public void setAdbScore(double adbScore) {
		this.adbScore = adbScore;
	}
	public double getDebuggableScore() {
		return debuggableScore;
	}
	public void setDebuggableScore(double debuggableScore) {
		this.debuggableScore = debuggableScore;
	}
	public double getMalwareScore() {
		return malwareScore;
	}
	public void setMalwareScore(double malwareScore) {
		this.malwareScore = malwareScore;
	}
	public double getMaximumRiskScore() {
		return maximumRiskScore;
	}
	public void setMaximumRiskScore(double maximumRiskScore) {
		this.maximumRiskScore = maximumRiskScore;
	}
	public double getGreenRisk() {
		return greenRisk;
	}
	public void setGreenRisk(double greenRisk) {
		this.greenRisk = greenRisk;
	}
	public double getYellowRisk() {
		return yellowRisk;
	}
	public void setYellowRisk(double yellowRisk) {
		this.yellowRisk = yellowRisk;
	}
	public double getOrangRisk() {
		return orangRisk;
	}
	public void setOrangRisk(double orangRisk) {
		this.orangRisk = orangRisk;
	}
	public double getRedRisk() {
		return redRisk;
	}
	public void setRedRisk(double redRisk) {
		this.redRisk = redRisk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getVirusTotalScore() {
		return virusTotalScore;
	}

	public void setVirusTotalScore(double virusTotalScore) {
		this.virusTotalScore = virusTotalScore;
	}

	public double getVirusTotalThreshold() {
		return virusTotalThreshold;
	}

	public void setVirusTotalThreshold(double virusTotalThreshold) {
		this.virusTotalThreshold = virusTotalThreshold;
	}

}
