'use strict';

import React, { useState, useEffect } from 'react'
import { UserPermissionClosure, Source } from '../../api/entities';
import { SOURCE_SERVICE_SAVE_DETAIL, SOURCE_SERVICE_READ_DETAIL, SOURCE_SERVICE_UPDATE_SUBSCRIBE } from '../../api/apiEndpoints';
import { asyncFetchGet, asyncFetchPost } from '../../api/apiCall';
import InputBox from '../InputBox';
import SourceAttributeEdit from './SourceAttributeEdit';
import { useNavigate, useParams } from 'react-router-dom';

const makeSubButton = (user: UserPermissionClosure, source: Source, updateSub: (value: string) => void, updateIgnore: (value: string) => void) => {
	if (source.ignored) {
		return (<button disabled={!user.registered} onClick={async () => updateIgnore("false")}>Unignore</button>);
	} else if (!source.subscribed) {
		return (<>
			<button disabled={!user.registered} onClick={async () => updateSub("true")}>Subscribe</button>
			<button disabled={!user.registered} onClick={async () => updateIgnore("true")}>Ignore</button>
		</>);
	} else {
		return (<button disabled={!user.registered} onClick={async () => updateSub("false")}>Unsubscribe</button>);
	}
}

type SourceChange = {
	name?: string;
	description?: string;
}

type Props = {
	user: UserPermissionClosure;
}

const SourceDetail: React.FC<Props> = ({ user }) => {
	const navigate = useNavigate();
	const { id } = useParams();
	const onBack = () => { navigate("/sources"); };
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(false);
	const [source, setSource] = useState<Source>(null);
	const [changes, setChanges] = useState<SourceChange>({});
	const [editAttr, setEditAttr] = useState(false);

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
			<button onClick={onBack}>Back to list</button>
			<p>Error: {error}</p>
			<button onClick={() => { setChanges({}); if (source === null) { loadData() } else { setError(null) } }}>Clear</button>
		</>);
	} else if (!isLoaded) {
		return (<>
				<button onClick={onBack}>Back to list</button>
				<div>Loading...</div>
			</>);
	} else {
		const updateSubImpl = async (sub: boolean, value: string) => {
			setIsLoaded(false);
			setError(null);
			const data = {
				id: id.toString(),
				subscribe: sub.toString(),
				value: value
			};
			await asyncFetchPost(SOURCE_SERVICE_UPDATE_SUBSCRIBE, data, setSource, setError, setIsLoaded);
		}
		const subButton = makeSubButton(user, source, value => updateSubImpl(true, value), value => updateSubImpl(false, value));

		const editAttributes = () => {
			setEditAttr(!editAttr);
		}

		const editAttrArea = editAttr ? <SourceAttributeEdit id={Number(id)} /> : <></>;

		if (user.editSource) {
			return (
				<>
					<button onClick={onBack}>Back to list</button>
					<h3>Source {id}</h3>
					<InputBox label="Name" initialValue={source.name} setValue={onName} />
					<br />
					<InputBox label="Description" initialValue={source.description} setValue={onDescription} />
					<br />
					<button onClick={onSave}>Save</button>
					<button onClick={editAttributes}>{editAttr ? "Close attributes" : "Edit attributes"}</button>
					{subButton}
					{editAttrArea}
				</>
			);
		} else {
			return (
				<>
					<button onClick={onBack}>Back to list</button>
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
