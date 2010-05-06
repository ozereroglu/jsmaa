/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/

package fi.smaa.jsmaa.model;

import fi.smaa.common.RandomUtil;

public final class MissingPreferenceInformation extends AbstractEntity implements PreferenceInformation {
	private static final long serialVersionUID = -8477410889345079220L;
	transient private double[] tmparr;
	private int numCrit;
	
	public MissingPreferenceInformation(int numCrit) {
		this.numCrit = numCrit;
	}

	public double[] sampleWeights() {
		initArray();
		RandomUtil.createSumToOneRand(tmparr);
		return tmparr;
	}
	
	private void initArray() {
		if (tmparr == null) {
			tmparr = new double[numCrit];			
		}
	}

	public MissingPreferenceInformation deepCopy() {
		return new MissingPreferenceInformation(numCrit);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MissingPreferenceInformation)) {
			return false;
		}
		MissingPreferenceInformation mo = (MissingPreferenceInformation) other;
		return numCrit == mo.numCrit;
	}		
}