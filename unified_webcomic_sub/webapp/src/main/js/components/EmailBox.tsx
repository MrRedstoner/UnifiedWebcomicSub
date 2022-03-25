'use strict';

import React, { useState } from 'react';

type Props = {
	label: string;
	initialValue: string;
	setValue: (value: string) => void;
};

const EmailBox: React.FC<Props> = ({ label, initialValue, setValue: setParentValue }) => {
	const [value, setValue] = useState(initialValue);

	const onChange = (newValue: string) => {
		setValue(newValue);
		setParentValue(newValue);
	};

	return (
		<label>
			{label + " "}
			<input type="email" onChange={(event) => onChange(event.target.value)} value={value}></input>
		</label>)
};

export default EmailBox;