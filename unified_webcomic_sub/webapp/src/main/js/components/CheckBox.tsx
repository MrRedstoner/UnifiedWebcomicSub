'use strict';

import React, { useState } from 'react';

type Props = {
	label: string;
	initialValue: boolean;
	setValue: (value: boolean) => void;
	disabled?: boolean;
};

const CheckBox: React.FC<Props> = ({ label, initialValue, setValue: setParentValue, disabled }) => {
	const [value, setValue] = useState(initialValue);
	const onChange = () => {
		setValue(!value);
		setParentValue(!value);
	};
	return (
		<label>
			{label + " "}
			<input type="checkbox" defaultChecked={value} onChange={onChange} disabled={disabled}></input>
		</label>)
};

export default CheckBox;