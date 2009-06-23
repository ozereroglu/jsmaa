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


public class OrdinalCriterion extends AbstractCriterion {
	
	private static final long serialVersionUID = -1153156807411990038L;

	public OrdinalCriterion(String name) {
		super(name);
	}

	public OrdinalCriterion deepCopy() {
		return new OrdinalCriterion(name);
	}

	@Override
	public String getTypeLabel() {
		return "Ordinal";
	}	
}