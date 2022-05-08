'use strict';

import React, { useState } from 'react';
import { UserPermissionClosure, Group } from "../../api/entities";
import InputBox from '../InputBox';
import { asyncFetchPost } from '../../api/apiCall';
import { GROUP_SERVICE_CREATE } from '../../api/apiEndpoints';
import { useNavigate } from 'react-router-dom';

type Props = {
	user: UserPermissionClosure
}

const GroupCreate: React.FC<Props> = ({ user }) => {
	const navigate = useNavigate();
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(true);
	const [group, setGroup] = useState<Group>({ id: null, name: "", description: "" });
	const onCreated = (id: number)=>{
		navigate("/groups/show/" + id);
	}

	const onName = (name: string) => {
		const newGroup = Object.assign({}, group);
		newGroup.name = name;
		setGroup(newGroup);
	};

	const onDescription = (description: string) => {
		const newGroup = Object.assign({}, group);
		newGroup.description = description;
		setGroup(newGroup);
	};

	const onSave = async () => {
		const data = {
			name: group.name,
			description: group.description
		};
		await asyncFetchPost(GROUP_SERVICE_CREATE, data, onCreated, setError, setIsLoaded);
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
			<h3>New group</h3>
			<InputBox label="Name" initialValue={group.name} setValue={onName} />
			<br />
			<InputBox label="Description" initialValue={group.description} setValue={onDescription} />
			<br />
			<button onClick={onSave} disabled={(!user.editGroup) || group.name === "" || group.description === ""}>Save</button>
		</>);
	}
};

export default GroupCreate;