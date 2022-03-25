'use strict';

import React, { useState, useMemo } from 'react';

type Props = {
	label: string;
	initialValue: number;
	valueList: string[];
	setValue: (value: number) => void;
};

const SimpleSelect: React.FC<Props> = ({ label, initialValue, setValue: setParentValue, valueList }) => {
	const [value, setValue] = useState(initialValue);
	const onChange = (newValue: number) => {
		setValue(newValue);
		setParentValue(newValue);
	};

	const options = useMemo(() => valueList.map((opt, index) =>
		<option key={index} value={index}>{opt}</option>), [valueList])

	return (
		<label>
			{label + " "}
			<select defaultValue={value} onChange={(event) => onChange(event.target.selectedIndex)}>
				{options}
			</select>
		</label>)
};

export default SimpleSelect;