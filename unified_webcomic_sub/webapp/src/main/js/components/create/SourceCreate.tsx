'use strict';

import React, { useState } from 'react'
import { UserPermissionClosure, Source } from '../../api/entities';
import { asyncFetchPost } from '../../api/apiCall';
import { SOURCE_SERVICE_CREATE } from '../../api/apiEndpoints';
import InputBox from '../InputBox';
import { useNavigate } from 'react-router-dom';

type Props = {
	user: UserPermissionClosure;
}

const SourceCreate: React.FC<Props> = ({ user }) => {
	const navigate = useNavigate();
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(true);
	const [source, setSource] = useState<Source>({ id: null, name: "", description: "" });
	const onCreated = (id: number)=>{
		navigate("/sources/show/" + id);
	}

	const onName = (name: string) => {
		const newSource = Object.assign({}, source);
		newSource.name = name;
		setSource(newSource);
	};

	const onDescription = (description: string) => {
		const newSource = Object.assign({}, source);
		newSource.description = description;
		setSource(newSource);
	};

	const onSave = async () => {
		const data = {
			name: source.name,
			description: source.description
		};
		await asyncFetchPost(SOURCE_SERVICE_CREATE, data, onCreated, setError, setIsLoaded);
	}

	if (error) {
		return (<>
			<p>Error: {error}</p>
			<button onClick={() => { setError(null) }}>Clear</button>
		</>);
	} else if (!isLoaded) {
		return <div>Saving...</div>;
	} else {
		return (<>
			<h3>New source</h3>
			<InputBox label="Name" initialValue={source.name} setValue={onName} />
			<br />
			<InputBox label="Description" initialValue={source.description} setValue={onDescription} />
			<br />
			<button onClick={onSave} disabled={(!user.editSource) || source.name === "" || source.description === ""}>Save</button>
		</>);
	}
}

export default SourceCreate;