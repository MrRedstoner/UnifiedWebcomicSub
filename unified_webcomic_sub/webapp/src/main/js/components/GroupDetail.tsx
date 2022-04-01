'use strict';

import React, { useState, useEffect } from 'react';
import { UserPermissionClosure, Group, GroupChild, SourceSubscription } from '../api/entities';
import InputBox from './InputBox';
import { asyncFetchGet, asyncFetchPost } from '../api/apiCall';
import { GROUP_SERVICE_READ_DETAIL, GROUP_SERVICE_SAVE_DETAIL, GROUP_SERVICE_UPDATE_SUBSCRIBE, GROUP_SERVICE_UPDATE_CHILDREN, GROUP_SERVICE_UPDATE_SOURCES } from '../api/apiEndpoints';

const makeSubButton = (user: UserPermissionClosure, group: Group, updateSub: (value: string) => void) => {
	if (group.parents.length === 0) {
		return (<button disabled={!user.registered} onClick={async () => updateSub("true")}>Subscribe</button>);
	} else {
		return (<button disabled={!user.registered} onClick={async () => updateSub("false")}>Unsubscribe</button>);
	}
}

type ChildrenProps = {
	canEdit: boolean
	children: GroupChild[];
	updateChildren?: (child_id: string, value: string) => void;
}

const GroupChildrenView: React.FC<ChildrenProps> = ({ canEdit, children, updateChildren }) => {
	const childIds = children.map((gc) => gc.child.id.toString());
	if (!canEdit) {
		return (<p>Children: {childIds.join(", ")}</p>);
	} else {
		const [childId, setChildId] = useState<string>("")
		const contains = childIds.includes(childId);
		return (<>
			<p>Children: {childIds.join(", ")}</p>
			<InputBox label="Id" initialValue={childId} setValue={setChildId} />
			<button disabled={childId === ""} onClick={async () => { updateChildren(childId, (!contains).toString()) }}>{contains ? "Remove" : "Add"}</button>
		</>);
	}
}

type SourcesProps = {
	canEdit: boolean
	sources: SourceSubscription[];
	updateSources?: (source_id: string, value: string) => void;
}

const GroupSourcesView: React.FC<SourcesProps> = ({ canEdit, sources, updateSources }) => {
	const childIds = sources.map((gc) => gc.source.id.toString());
	if (!canEdit) {
		return (<p>Sources: {childIds.join(", ")}</p>);
	} else {
		const [childId, setChildId] = useState<string>("")
		const contains = childIds.includes(childId);
		return (<>
			<p>Sources: {childIds.join(", ")}</p>
			<InputBox label="Id" initialValue={childId} setValue={setChildId} />
			<button disabled={childId === ""} onClick={async () => { updateSources(childId, (!contains).toString()) }}>{contains ? "Remove" : "Add"}</button>
		</>);
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
			<button onClick={() => { setChanges({}); if (group === null) { loadData() } else { setError(null) } }}>Clear</button>
		</>);
	} else if (!isLoaded) {
		return <div>Loading...</div>;
	} else {

		const updateChildren = async (child_id: string, value: string) => {
			setIsLoaded(false);
			setError(null);
			const data = {
				id: id.toString(),
				child: child_id,
				value: value
			};
			await asyncFetchPost(GROUP_SERVICE_UPDATE_CHILDREN, data, setGroup, setError, setIsLoaded);
		}

		const updateSources = async (source_id: string, value: string) => {
			setIsLoaded(false);
			setError(null);
			const data = {
				id: id.toString(),
				source: source_id,
				value: value
			};
			await asyncFetchPost(GROUP_SERVICE_UPDATE_SOURCES, data, setGroup, setError, setIsLoaded);
		}

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
					<h3>Group {id}</h3>
					<InputBox label="Name" initialValue={group.name} setValue={onName} />
					<br />
					<InputBox label="Description" initialValue={group.description} setValue={onDescription} />
					<br />
					<button onClick={onSave}>Save</button>
					{subButton}
					<GroupChildrenView canEdit={true} children={group.children} updateChildren={updateChildren} />
					<GroupSourcesView canEdit={true} sources={group.sources} updateSources={updateSources} />
				</>
			);
		} else {
			return (
				<>
					<h3>Group {id}</h3>
					<p>Name: {group.name}</p>
					<p>Description: {group.description}</p>
					{subButton}
					<GroupChildrenView canEdit={false} children={group.children} />
					<GroupSourcesView canEdit={false} sources={group.sources} />
				</>
			);
		}
	}
};

export default GroupDetail;