'use strict';

import React, { useState } from 'react';

type Props = {
	label: string;
	initialValue: boolean;
	setValue: (value: boolean) => void;
};

const CheckBox: React.FC<Props> = ({ label, initialValue, setValue: setParentValue }) => {
	const [value, setValue] = useState(initialValue);
	const onChange = () => {
		setValue(!value);
		setParentValue(!value);
	};
	return (
		<label>
			{label + " "}
			<input type="checkbox" defaultChecked={value} onChange={onChange}></input>
		</label>)
};

export default CheckBox;