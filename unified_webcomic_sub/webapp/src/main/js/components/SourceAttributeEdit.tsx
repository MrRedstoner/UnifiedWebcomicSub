'use strict';

import React, { useState, useEffect } from 'react'
import { asyncFetchGet, asyncFetchPost } from '../api/apiCall';
import { SOURCE_SERVICE_READ_ATTRIBUTES, SOURCE_SERVICE_SAVE_ATTRIBUTES } from '../api/apiEndpoints';
import InputBox from './InputBox';

type SourceAttrs = Record<string, string>;

type Attr = {
	name: string;
	value: string;
	onClick: (name: string, value: string) => void
}

const SourceAttributeRow: React.FC<Attr> = ({ name, value, onClick }) => {
	return <p onClick={e => onClick(name, value)}>{name}: {value}</p>;
}

type Props = {
	id: number
}

const SourceAttributeEdit: React.FC<Props> = ({ id }) => {
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(false);
	const [attrs, setAttrs] = useState<SourceAttrs>(null);
	const [inName, setInName] = useState<string>("");
	const [inValue, setInValue] = useState<string>("");

	const loadData = async () => {
		setIsLoaded(false);
		setError(null);
		const data = { id: id.toString() };
		await asyncFetchGet(SOURCE_SERVICE_READ_ATTRIBUTES, data, setAttrs, setError, setIsLoaded);
	}

	useEffect(() => {
		loadData().catch(console.error);
	}, [id]);

	const updateValue = () => {
		const newAttrs = Object.assign({}, attrs);
		newAttrs[inName] = inValue;
		setAttrs(newAttrs);
	}

	const delValue = () => {
		const newAttrs = Object.assign({}, attrs);
		delete newAttrs[inName];
		setAttrs(newAttrs);
	}

	const onSave = async () => {
		const data = { id: id.toString(), attributes: attrs };
		setIsLoaded(false);
		setError(null);
		await asyncFetchPost(SOURCE_SERVICE_SAVE_ATTRIBUTES, data, setAttrs, setError, setIsLoaded);
	}

	const showAttr = (name: string, value: string) => {
		//TODO
		//setInName(name);
		//setInValue(value);
	}

	if (error) {
		return <p>Error: {error}</p>;
	} else if (!isLoaded) {
		return <div>Loading...</div>;
	} else {
		const attrRows = Object.keys(attrs).map((name) => <SourceAttributeRow onClick={showAttr} key={name} name={name} value={attrs[name]} />);

		return (<div>
			{attrRows}
			<InputBox label="Name" initialValue={inName} setValue={setInName} />
			<InputBox label="Value" initialValue={inValue} setValue={setInValue} />
			<button disabled={inName === "" || inValue === ""} onClick={updateValue}>Update</button>
			<button disabled={inName === ""} onClick={delValue}>Delete</button>
			<button onClick={onSave}>Save</button>
		</div>);
	}
}

export default SourceAttributeEdit;