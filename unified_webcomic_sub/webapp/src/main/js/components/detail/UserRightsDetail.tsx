'use strict';

import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from "react-router-dom";
import { UWSUser, UserPermissionClosure } from '../../api/entities';
import { USER_SERVICE_USERS_READ_DETAIL, USER_SERVICE_USERS_UPDATE_PERMS } from '../../api/apiEndpoints';
import { asyncFetchGetState, asyncFetchPostState } from '../../api/apiCall';
import CheckBox from '../CheckBox';

type ItemState = {
	isLoaded: boolean;
	error: string | null;
	item: UWSUser;
}

const notLoaded: ItemState = { isLoaded: false, error: null, item: null }

type PermChanges = {
	admin?: boolean;
	createPost?: boolean;
	createSource?: boolean;
	editSource?: boolean;
	editGroup?: boolean;
}

type Props = {
	user: UserPermissionClosure;
}

const UserRightsDetail: React.FC<Props> = ({ user }) => {
	const navigate = useNavigate();
	const { id } = useParams();
	const onBack = () => { navigate("/users"); };
	const [itemState, setItemState] = useState<ItemState>(notLoaded);
	const [changes, setChanges] = useState<PermChanges>({});

	const loadData = () => {
		asyncFetchGetState(
			USER_SERVICE_USERS_READ_DETAIL,
			{ id: id },
			(item: UWSUser) => setItemState({ isLoaded: true, error: null, item: item }),
			error => setItemState({ isLoaded: true, error: error, item: null }));
	}

	useEffect(() => {
		loadData();
	}, [id]);

	const setAdmin = (admin: boolean) => {
		const newChanges = Object.assign({}, changes);
		newChanges.admin = admin;
		setChanges(newChanges);
	}

	const setCreatePost = (val: boolean) => {
		const newChanges = Object.assign({}, changes);
		newChanges.createPost = val;
		setChanges(newChanges);
	}

	const setCreateSource = (val: boolean) => {
		const newChanges = Object.assign({}, changes);
		newChanges.createSource = val;
		setChanges(newChanges);
	}

	const setEditSource = (val: boolean) => {
		const newChanges = Object.assign({}, changes);
		newChanges.editSource = val;
		setChanges(newChanges);
	}

	const setEditGroup = (val: boolean) => {
		const newChanges = Object.assign({}, changes);
		newChanges.editGroup = val;
		setChanges(newChanges);
	}

	if (itemState.error) {
		return (<>
			<button onClick={onBack}>Back to list</button>
			<p>Error: {itemState.error}</p>
		</>);
	} else if (!itemState.isLoaded) {
		return (<>
			<button onClick={onBack}>Back to list</button>
			<div>Loading...</div>
		</>);
	} else {
		//TODO save changes
		const onSave = async () => {
			setItemState(notLoaded);
			const data:any = {
				id: id.toString()
			};
			if(changes.admin!==undefined)data.admin=changes.admin.toString();
			if(changes.createPost!==undefined)data.createPost=changes.createPost.toString();
			if(changes.createSource!==undefined)data.createSource=changes.createSource.toString();
			if(changes.editSource!==undefined)data.editSource=changes.editSource.toString();
			if(changes.editGroup!==undefined)data.editGroup=changes.editGroup.toString();
			await asyncFetchPostState(
				USER_SERVICE_USERS_UPDATE_PERMS,
				data,
				(item: UWSUser) => setItemState({ isLoaded: true, error: null, item: item }),
				error => setItemState({ isLoaded: true, error: error, item: null }));
		}

		return (<>
			<button onClick={onBack}>Back to list</button>
			<h3>User {itemState.item.id}</h3>
			<p>Name: {itemState.item.name}</p>
			<CheckBox label="Owner" initialValue={itemState.item.owner} setValue={() => { }} disabled={true} />
			<br/>
			<CheckBox label="Admin" initialValue={itemState.item.admin} setValue={setAdmin} disabled={!user.owner} />
			<br/>
			<CheckBox label="Post" initialValue={itemState.item.createPost} setValue={setCreatePost} disabled={!user.admin} />
			<br/>
			<CheckBox label="Source create" initialValue={itemState.item.createSource} setValue={setCreateSource} disabled={!user.admin} />
			<br/>
			<CheckBox label="Source edit" initialValue={itemState.item.editSource} setValue={setEditSource} disabled={!user.admin} />
			<br/>
			<CheckBox label="Group edit" initialValue={itemState.item.editGroup} setValue={setEditGroup} disabled={!user.admin} />
			<br/>
			<button onClick={onSave}>Save changes</button>
		</>);
	}
}

export default UserRightsDetail;