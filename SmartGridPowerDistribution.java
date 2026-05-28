public class SmartGridPowerDistribution {

	// Central Distribution Engine (Dynamic Polymorphism Dispatcher)
	public static void distributePower(PowerPlant plant) {
		System.out.printf("%n--- Analyzing Grid Connection: %s ---%n", plant.getPlantId());

		// 1. Dynamic Polymorphism - Runtime execution binds to the correct overridden
		// subclass logic
		double loss = plant.calculateGridLoss();
		double netPower = plant.getCurrentOutput() - loss;

		System.out.printf("Gross Generated Output: %.2f MW%n", plant.getCurrentOutput());
		System.out.printf("Grid Transmission Loss: %.2f MW%n", loss);
		System.out.printf("Net Power Delivered to City: %.2f MW%n", netPower);

		// 2. Safe Runtime Checking: Dynamic Type Inspection / Subclass Interrogation
		if (plant instanceof NuclearPowerPlant nuclearPlant) {
			if (nuclearPlant.isMeltdownImminent()) {
				System.out.println("CRITICAL ALERT: Meltdown imminent! Engaging emergency cooling systems!");
			}
		}
	}

	public static void main(String[] args) {
		// 1. Create a Solar Plant (ID: SOLAR-ALPHA, Output: 150.0 MW, Battery
		// Efficiency: 92%)
		SolarPowerPlant solarGrid = new SolarPowerPlant("SOLAR-ALPHA", 150.0, 0.92);

		// 2. Create a Nuclear Plant (ID: NUC-CORE-1, Output: 800.0 MW, Safety Limit:
		// 1000.0 MW)
		NuclearPowerPlant nuclearGrid = new NuclearPowerPlant("NUC-CORE-1", 800.0, 1000.0);

		System.out.println("=== SMART CORE GRID SYSTEM ONLINE ===");

		// Test Solar Connection
		distributePower(solarGrid);

		// Test Safe Nuclear Connection
		distributePower(nuclearGrid);

		// Test Dangerous Nuclear Boost Over Safety Limit
		System.out.println("\n[Action] Grid demand spiking! Forcing Nuclear power boost...");
		nuclearGrid.boostPower(250.0); // 800 + 250 = 1050 (Exceeds 1000 limit!)

		// Re-analyze Nuclear Grid Status after boost load modification
		distributePower(nuclearGrid);
	}
}

class PowerPlant {

	protected String plantId;
	private double currentOutput;

	// Constructor
	public PowerPlant(String plantId, double currentOutput) {
		this.plantId = plantId;
		this.currentOutput = currentOutput;
	}

	public String getPlantId() {
		return plantId;
	}

	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	public double getCurrentOutput() {
		return currentOutput;
	}

	public void setCurrentOutput(double currentOutput) {
		if (currentOutput >= 0) {
			this.currentOutput = currentOutput;
		} else {
			System.out.println("Error: Power output cannot be negative.");
		}
	}

	public double calculateGridLoss() {
		// FIXED: Removed the assignment operator '=' that was mutating currentOutput
		return this.currentOutput * 0.05;
	}
}

// Subclass
class SolarPowerPlant extends PowerPlant {

	private double batteryEfficiency; // FIXED: Added missing field declaration

	public SolarPowerPlant(String plantId, double currentOutput, double batteryEfficiency) {
		super(plantId, currentOutput);
		this.batteryEfficiency = batteryEfficiency;
	}

	public double getBatteryEfficiency() {
		return batteryEfficiency;
	}

	// Override this method from Power Plant
	@Override
	public double calculateGridLoss() {
		return this.getCurrentOutput() * 0.02;
	}
}

class NuclearPowerPlant extends PowerPlant {

	private final double maxSafetyLimit;
	protected boolean isMeltdownImminent;

	// FIXED: Parameter renamed from batteryEfficiency to maxSafetyLimit
	public NuclearPowerPlant(String plantId, double currentOutput, double maxSafetyLimit) {
		super(plantId, currentOutput);
		// FIXED: Correctly assigning parameters to fields
		this.maxSafetyLimit = maxSafetyLimit;
		this.isMeltdownImminent = false;
	}

	// FIXED: Added missing @Override annotation
	@Override
	public double calculateGridLoss() {
		return this.getCurrentOutput() * 0.08;
	}

	public boolean isMeltdownImminent() {
		return this.isMeltdownImminent;
	}

	public void boostPower(double extraMegawatts) {
		// FIXED: Fixed spelling typos
		double proposedOutput = getCurrentOutput() + extraMegawatts;

		if (proposedOutput > this.maxSafetyLimit) {
			this.setCurrentOutput(this.maxSafetyLimit);
			this.isMeltdownImminent = true;
			System.out.println("Boost request threshold exceeded by adding: " + extraMegawatts + " MW");
		} else {
			this.setCurrentOutput(proposedOutput);
		}
	}
}