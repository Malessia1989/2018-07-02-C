package it.polito.tdp.extflightdelays.model;

public class Rotta {

		Airport partenza;
		Airport dest;
		double peso;
		
		public Rotta(Airport partenza, Airport dest, double peso) {
			super();
			this.partenza = partenza;
			this.dest = dest;
			this.peso = peso;
		}

		public Airport getPartenza() {
			return partenza;
		}

		public void setPartenza(Airport partenza) {
			this.partenza = partenza;
		}

		public Airport getDest() {
			return dest;
		}

		public void setDest(Airport dest) {
			this.dest = dest;
		}

		public double getPeso() {
			return peso;
		}

		public void setPeso(double peso) {
			this.peso = peso;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dest == null) ? 0 : dest.hashCode());
			result = prime * result + ((partenza == null) ? 0 : partenza.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Rotta other = (Rotta) obj;
			if (dest == null) {
				if (other.dest != null)
					return false;
			} else if (!dest.equals(other.dest))
				return false;
			if (partenza == null) {
				if (other.partenza != null)
					return false;
			} else if (!partenza.equals(other.partenza))
				return false;
			return true;
		}
		
}
