'use strict';

import React, { useState } from 'react';

type Props = {
	label?: string;
	initialValue: string;
	setValue: (value: string) => void;
	className?: string
};

const InputBox: React.FC<Props> = ({ label, initialValue, setValue: setParentValue, className}) => {
	const [value, setValue] = useState(initialValue);
	const [initVal, setInitVal] = useState<string>("");
	if (initVal !== initialValue) {
		setInitVal(initialValue);
		setValue(initialValue);
	}

	const onChange = (newValue: string) => {
		setValue(newValue);
		setParentValue(newValue);
	};

	if (label) {
		return (
			<label>
				{label + " "}
				<input type="text" onChange={(event) => onChange(event.target.value)} value={value} className={className}></input>
			</label>);
	} else {
		return (
			<input type="text" onChange={(event) => onChange(event.target.value)} value={value} className={className}></input>);
	}

};

export default InputBox;