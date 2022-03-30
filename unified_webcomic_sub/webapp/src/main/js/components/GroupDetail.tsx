'use strict';

import React, { useState, useEffect } from 'react';
import { UserPermissionClosure, Group } from '../api/entities';
import InputBox from './InputBox';
import { asyncFetchGet, asyncFetchPost } from '../api/apiCall';
import { GROUP_SERVICE_READ_DETAIL, GROUP_SERVICE_SAVE_DETAIL, GROUP_SERVICE_UPDATE_SUBSCRIBE } from '../api/apiEndpoints';

const makeSubButton = (user: UserPermissionClosure, group: Group, updateSub: (value: string) => void) => {
	if (group.parents.length === 0) {
		return (<button disabled={!user.registered} onClick={async () => updateSub("true")}>Subscribe</button>);
	} else {
		return (<button disabled={!user.registered} onClick={async () => updateSub("false")}>Unsubscribe</button>);
	}
}

type GroupChange = {
	name?: string;
	description?: string;
}

type Props = {
	id: number;
	user: UserPermissionClosure;
}

const GroupDetail: React.FC<Props> = ({ id, user }) => {
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(false);
	const [group, setGroup] = useState<Group>(null);
	const [changes, setChanges] = useState<GroupChange>({});

	const onSave = async () => {
		const data = Object.assign({ id: id.toString() }, changes);
		setIsLoaded(false);
		setError(null);
		await asyncFetchPost(GROUP_SERVICE_SAVE_DETAIL, data, setGroup, setError, setIsLoaded);
	}

	const loadData = async () => {
		setIsLoaded(false);
		setError(null);
		const data = { id: id.toString() };
		await asyncFetchGet(GROUP_SERVICE_READ_DETAIL, data, setGroup, setError, setIsLoaded);
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
			<button onClick={() => { setError(null) }}>Clear</button>
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
			await asyncFetchPost(GROUP_SERVICE_UPDATE_SUBSCRIBE, data, setGroup, setError, setIsLoaded);
		}
		const subButton = makeSubButton(user, group, updateSub);

		//TODO allow showing with null id for create
		if (user.editGroup) {
			return (
				<>
					<p>Group {id}</p>
					<InputBox label="Name" initialValue={group.name} setValue={onName} />
					<br />
					<InputBox label="Description" initialValue={group.description} setValue={onDescription} />
					<br />
					<button onClick={onSave}>Save</button>
					{subButton}
				</>
			);
		} else {
			return (
				<>
					<p>Group {id}</p>
					<p>Name: {group.name}</p>
					<p>Description: {group.description}</p>
					{subButton}
				</>
			);
		}
	}
};

export default GroupDetail;