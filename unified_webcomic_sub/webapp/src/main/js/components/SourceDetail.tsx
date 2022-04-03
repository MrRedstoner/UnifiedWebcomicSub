'use strict';

import React, { useState, useEffect } from 'react'
import { UserPermissionClosure, Source } from '../api/entities';
import { SOURCE_SERVICE_SAVE_DETAIL, SOURCE_SERVICE_READ_DETAIL, SOURCE_SERVICE_UPDATE_SUBSCRIBE } from '../api/apiEndpoints';
import { asyncFetchGet, asyncFetchPost } from '../api/apiCall';
import InputBox from './InputBox';

const makeSubButton = (user: UserPermissionClosure, source: Source, updateSub: (value: string) => void) => {
	if (source.subscriptions.length === 0) {
		return (<button disabled={!user.registered} onClick={async () => updateSub("true")}>Subscribe</button>);
	} else {
		return (<button disabled={!user.registered} onClick={async () => updateSub("false")}>Unsubscribe</button>);
	}
}

type SourceChange = {
	name?: string;
	description?: string;
}

type Props = {
	id: number;
	user: UserPermissionClosure;
}

const SourceDetail: React.FC<Props> = ({ id, user }) => {
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(false);
	const [source, setSource] = useState<Source>(null);
	const [changes, setChanges] = useState<SourceChange>({});

	const onSave = async () => {
		const data = Object.assign({ id: id.toString() }, changes);
		setIsLoaded(false);
		setError(null);
		await asyncFetchPost(SOURCE_SERVICE_SAVE_DETAIL, data, setSource, setError, setIsLoaded);
	}

	const loadData = async () => {
		setIsLoaded(false);
		setError(null);
		const data = { id: id.toString() };
		await asyncFetchGet(SOURCE_SERVICE_READ_DETAIL, data, setSource, setError, setIsLoaded);
	}

	useEffect(() => {
		loadData().catch(console.error);
	}, [id]);

	const onName = (name: string) => {
		const newChanges = Object.assign({}, changes);
		newChanges.name = name;
		setChanges(newChanges);
	};

	const onDescription = (description: string) => {
		const newChanges = Object.assign({}, changes);
		newChanges.description = description;
		setChanges(newChanges);
	};

	if (error) {
		return (<>
			<p>Error: {error}</p>
			<button onClick={() => { setChanges({}); if (source === null) { loadData() } else { setError(null) } }}>Clear</button>
		</>);
	} else if (!isLoaded) {
		return <div>Loading...</div>;
	} else {
		const updateSub = async (value: string) => {
			setIsLoaded(false);
			setError(null);
			const data = {
				id: id.toString(),
				value: value
			};
			await asyncFetchPost(SOURCE_SERVICE_UPDATE_SUBSCRIBE, data, setSource, setError, setIsLoaded);
		}
		const subButton = makeSubButton(user, source, updateSub);

		//TODO allow showing with null id for create
		if (user.editSource) {
			return (
				<>
					<h3>Source {id}</h3>
					<InputBox label="Name" initialValue={source.name} setValue={onName} />
					<br />
					<InputBox label="Description" initialValue={source.description} setValue={onDescription} />
					<br />
					<button onClick={onSave}>Save</button>
					{subButton}
				</>
			);
		} else {
			return (
				<>
					<h3>Source {id}</h3>
					<p>Name: {source.name}</p>
					<p>Description: {source.description}</p>
					{subButton}
				</>
			);
		}
	}
}

export default SourceDetail;
