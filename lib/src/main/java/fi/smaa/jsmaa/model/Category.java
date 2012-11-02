/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

public class Category extends Alternative {
	
	private static final long serialVersionUID = 8243454311923512118L;

	public Category(String name) {
		super(name);
	}
	
	@Override
	public Category deepCopy() {
		Category a = new Category(getName());
		return a;
	}
		
	@SuppressWarnings("unused")
	private static final XMLFormat<Category> XML = new XMLFormat<Category>(Category.class) {		
		@Override
		public boolean isReferenceable() {
			return true;
		}
		@Override
		public Category newInstance(Class<Category> cls, InputElement ie) throws XMLStreamException {
			return new Category(ie.getAttribute("name", ""));
		}
		@Override
		public void read(InputElement ie, Category alt) throws XMLStreamException {
			alt.setName(ie.getAttribute("name", ""));
		}
		@Override
		public void write(Category alt, OutputElement oe) throws XMLStreamException {
			oe.setAttribute("name", alt.getName());
		}		
	};	

}