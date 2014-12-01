package at.ac.uibk.dps.biohadoop.algorithms.mopso;

import java.util.ArrayList;
import java.util.List;

public class Archive {

	private final int archiveSize;
	private List<Particle> particles = new ArrayList<>();
	int count = 0;

	public Archive(int archiveSize) {
		this.archiveSize = archiveSize;
	}

	public boolean offer(Particle particle) {
//		if (particles.size() == 0) {
//			particles.add(particle);
//			return true;
//		}
//		for (int i = 0; i < particles.size(); i++) {
//			double[] pbestValue0 = particle.getPBestValue();
//			double[] pbestValue1 = particles.get(i).getPBestValue();
//			if (pbestValue0[0] == pbestValue1[0] && pbestValue0[1] == pbestValue1[1]) {
//				return false;
//			}
//		}
			
		Particle newParticle = new Particle(particle);
		particles.add(newParticle);
		List<List<Integer>> fronts = getNonDominatedSolutions(particles);
		List<Particle> newArchive = new ArrayList<>();
		
		for (int i = 0; i < Math.min(archiveSize, fronts.get(0).size()); i++) {
			newArchive.add(particles.get(fronts.get(0).get(i)));
		}
		
//		int currentRank = 0;
//		while(newArchive.size() < archiveSize && currentRank < fronts.size()) {
//			List<Integer> front = fronts.get(currentRank);
//			if (newArchive.size() + front.size() <= archiveSize) {
//				for (int individualIndex : front) {
//					newArchive.add(particles.get(individualIndex));
//				}
//			}
//			else {
//				CrowdingDistance.sortFrontAccordingToCrowdingDistance(
//						front, particles);
//				int missingElements = archiveSize - newArchive.size();
//				for (int n = 0; n < missingElements; n++) {
//					newArchive.add(particles.get(front.get(n)));
//				}
//			}
//			currentRank++;
//		}
		particles = newArchive;
		boolean particleAccepted = particles.contains(newParticle);
		if (particleAccepted) {
			System.out.println(count++);
			newParticle.setPBestValue(newParticle.getCurrentValue());
		}
		return particleAccepted;
		
//		for (int i = 0; i < particles.size(); i++) {
////			double[] p = particle.getPBestValue();
////			double[] q = particles.get(i).getPBestValue();
////			if (dominates(p, q)) {
//				// If particle dominates any archive element, it can be added to
//				// the archive
//				particles.add(particle);
////				System.out.println("size before: " + particles.size());
//				
//				List<List<Integer>> fronts = getNonDominatedSolutions(particles);
//				List<Particle> newArchive = new ArrayList<>();
//				
//				int currentRank = 0;
//				while(newArchive.size() < archiveSize && currentRank < fronts.size()) {
//					List<Integer> front = fronts.get(currentRank);
//					if (newArchive.size() + front.size() <= archiveSize) {
//						for (int individualIndex : front) {
//							newArchive.add(particles.get(individualIndex));
//						}
//					}
//					else {
//						CrowdingDistance.sortFrontAccordingToCrowdingDistance(
//								front, particles);
//						int missingElements = archiveSize - newArchive.size();
//						for (int n = 0; n < missingElements; n++) {
//							newArchive.add(particles.get(front.get(n)));
//						}
//					}
//					currentRank++;
//				}
//				particles = newArchive;
//				
////				particles = getNonDominatedSolutions(particles);
//				System.out.println("size after: " + particles.size());
//
//				if (particles.size() > archiveSize) {
//					System.out.println("size: " + particles.size());
//					// particles = shrinkWithCrowdingDistance(particles);
//				}
//
////				return true;
////			} else if (dominates(q, p)) {
////				// If the offered particle is dominated by any particle in the
////				// archive, it is not included in the archive
////				return false;
////			} else {
////				// The particle and all archive particles are non-dominated, it is save
////				// to add the particle to the archive
////				particles.add(particle);
////
////				List<List<Integer>> fronts = getNonDominatedSolutions(particles);
////				List<Particle> newArchive = new ArrayList<>();
////				
////				int currentRank = 0;
////				while(newArchive.size() < archiveSize && currentRank < fronts.size()) {
////					List<Integer> front = fronts.get(currentRank);
////					if (newArchive.size() + front.size() <= archiveSize) {
////						for (int individualIndex : front) {
////							newArchive.add(particles.get(individualIndex));
////						}
////					}
////					else {
////						CrowdingDistance.sortFrontAccordingToCrowdingDistance(
////								front, particles);
////						int missingElements = archiveSize - newArchive.size();
////						for (int n = 0; n < missingElements; n++) {
////							newArchive.add(particles.get(front.get(n)));
////						}
////					}
////					currentRank++;
////				}
////				particles = newArchive;
////				
////				System.out.println("size: " + particles.size());
////				return true;
////			}
//		}
//
//		// The particle and all archive particles are non-dominated, it is save
//		// to add the particle to the archive
//		particles.add(particle);
//		particles = getNonDominatedSolutions(particles);
//
//		if (particles.size() > archiveSize) {
//			System.out.println("size: " + particles.size());
//			// particles = shrinkWithCrowdingDistance(particles);
//		}
	}
	
	public List<Particle> getForPrint() {
		List<List<Integer>> fronts = getNonDominatedSolutions(particles);
		List<Particle> nonDominated = new ArrayList<>();
		for (int index : fronts.get(0)) {
			nonDominated.add(particles.get(index));
		}
//		for (int index : fronts.get(1)) {
//			nonDominated.add(particles.get(index));
//		}
		return nonDominated;
	}

	public Particle getRandomMember() {
		return particles.get(particles.size() - 1);
	}

	public Particle getMember(int i) {
		if (i < particles.size()) {
			return particles.get(i);
		}
		return null;
	}

	
	
//	// taken from http://repository.ias.ac.in/83498/1/2-a.pdf
//	/**
//	 * Computes the front, where the main List holds the fronts, e.g.
//	 * frontRanking.get(0) means the first front, frontRanking.get(1) the second
//	 * front... The entries of each front point to the index of the individual
//	 * and its corresponding objective in the population
//	 * 
//	 * @param objectives
//	 * @return
//	 */
//	private List<Particle> getNonDominatedSolutions(List<Particle> particles) {
//		int size = particles.size();
//
//		List<Particle> nonDominated = new ArrayList<>();
//
//		List<List<Integer>> S = new ArrayList<List<Integer>>();
//		for (int i = 0; i < size + 1; i++) {
//			S.add(new ArrayList<Integer>());
//		}
//
//		int[] n = new int[size];
//
//		for (int i = 0; i < size; i++) {
//			for (int j = 0; j < size; j++) {
//				double[] p = particles.get(i).getPBestValue();
//				double[] q = particles.get(j).getPBestValue();
//				// if p (= combinedPopulation[i]) dominates q (=
//				// combinedPopulation[j]) then include q in Sp (which is S[i])
//				if (dominates(p, q)) {
//					// solution i dominates solution j
//					S.get(i).add(j);
//				}
//				// if p is dominated by q then increment np (which is n[i])
//				else if (dominates(q, p)) {
//					// solution i is dominated by an additional other solution
//					n[i]++;
//				}
//			}
//			// if no solution dominates p then p is a member of the first front
//			if (n[i] == 0) {
//				nonDominated.add(particles.get(i));
//			}
//		}
//		return nonDominated;
//	}
	
	public List<List<Integer>> getNonDominatedSolutions(List<Particle> particles) {
		int size = particles.size();
		
		List<List<Integer>> fronts = new ArrayList<List<Integer>>();
		fronts.add(new ArrayList<Integer>());

		List<List<Integer>> S = new ArrayList<List<Integer>>();
		for (int i = 0; i < size + 1; i++) {
			S.add(new ArrayList<Integer>());
		}

		int[] n = new int[size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				double[] p = particles.get(i).getCurrentValue();
				double[] q = particles.get(j).getCurrentValue();
				// if p (= combinedPopulation[i]) dominates q (=
				// combinedPopulation[j]) then include q in Sp (which is S[i])
				if (dominates(p, q)) {
					// solution i dominates solution j
					S.get(i).add(j);
				}
				// if p is dominated by q then increment np (which is n[i])
				else if (dominates(q, p)) {
					// solution i is dominated by an additional other solution
					n[i]++;
				}
			}
			// if no solution dominates p then p is a member of the first front
			if (n[i] == 0) {
				fronts.get(0).add(i);
			}
		}

		int i = 0;
		while (!fronts.get(i).isEmpty()) {
			List<Integer> H = new ArrayList<Integer>();
			for (int memberP : fronts.get(i)) {
				for (int memberQ : S.get(memberP)) {
					n[memberQ]--;
					if (n[memberQ] == 0) {
						H.add(memberQ);
					}
				}
			}
			i++;
			fronts.add(H);
		}
		fronts.remove(fronts.size() - 1);
		return fronts;
	}

	public boolean dominates(double[] ds, double[] ds2) {
		boolean lesser = false;
		for (int i = 0; i < ds.length; i++) {
			if (ds[i] > ds2[i]) {
				return false;
			}
			if (ds[i] < ds2[i]) {
				lesser = true;
			}
		}
		return lesser;
	}

}
