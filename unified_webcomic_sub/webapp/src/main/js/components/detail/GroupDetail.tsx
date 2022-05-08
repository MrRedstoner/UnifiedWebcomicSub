'use strict';

import React, { useState, useEffect } from 'react';
import { UserPermissionClosure, Group, GroupChild, SourceSubscription, PostSubscription } from '../../api/entities';
import InputBox from '../InputBox';
import { asyncFetchGet, asyncFetchPost } from '../../api/apiCall';
import { GROUP_SERVICE_READ_DETAIL, GROUP_SERVICE_SAVE_DETAIL, GROUP_SERVICE_UPDATE_SUBSCRIBE, GROUP_SERVICE_UPDATE_CHILDREN, GROUP_SERVICE_UPDATE_SOURCES, GROUP_SERVICE_UPDATE_POSTERS } from '../../api/apiEndpoints';
import { useNavigate, useParams } from 'react-router-dom';

const makeSubButton = (user: UserPermissionClosure, group: Group, updateSub: (value: string) => void) => {
	if (!group.subscribed) {
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

type PostersProps = {
	canEdit: boolean
	posters: PostSubscription[];
	updatePosters?: (source_id: string, value: string) => void;
}

const GroupPostersView: React.FC<PostersProps> = ({ canEdit, posters, updatePosters }) => {
	const childIds = posters.map((gc) => gc.user.id.toString());
	if (!canEdit) {
		return (<p>Posters: {childIds.join(", ")}</p>);
	} else {
		const [childId, setChildId] = useState<string>("")
		const contains = childIds.includes(childId);
		return (<>
			<p>Posters: {childIds.join(", ")}</p>
			<InputBox label="Id" initialValue={childId} setValue={setChildId} />
			<button disabled={childId === ""} onClick={async () => { updatePosters(childId, (!contains).toString()) }}>{contains ? "Remove" : "Add"}</button>
		</>);
	}
}

type GroupChange = {
	name?: string;
	description?: string;
}

type Props = {
	user: UserPermissionClosure;
}

const GroupDetail: React.FC<Props> = ({ user }) => {
	const navigate = useNavigate();
	const { id } = useParams();
	const onBack = () => { navigate("/groups"); };
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
			<button onClick={onBack}>Back to list</button>
			<p>Error: {error}</p>
			<button onClick={() => { setChanges({}); if (group === null) { loadData() } else { setError(null) } }}>Clear</button>
		</>);
	} else if (!isLoaded) {
		return (<>
				<button onClick={onBack}>Back to list</button>
				<div>Loading...</div>
			</>);
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

		const updatePosters = async (user_id: string, value: string) => {
			setIsLoaded(false);
			setError(null);
			const data = {
				id: id.toString(),
				user: user_id,
				value: value
			};
			await asyncFetchPost(GROUP_SERVICE_UPDATE_POSTERS, data, setGroup, setError, setIsLoaded);
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

		if (user.editGroup) {
			return (
				<>
					<button onClick={onBack}>Back to list</button>
					<h3>Group {id}</h3>
					<InputBox label="Name" initialValue={group.name} setValue={onName} />
					<br />
					<InputBox label="Description" initialValue={group.description} setValue={onDescription} />
					<br />
					<button onClick={onSave}>Save</button>
					{subButton}
					<GroupChildrenView canEdit={true} children={group.children} updateChildren={updateChildren} />
					<GroupSourcesView canEdit={true} sources={group.sources} updateSources={updateSources} />
					<GroupPostersView canEdit={true} posters={group.posters} updatePosters={updatePosters} />
				</>
			);
		} else {
			return (
				<>
					<button onClick={onBack}>Back to list</button>
					<h3>Group {id}</h3>
					<p>Name: {group.name}</p>
					<p>Description: {group.description}</p>
					{subButton}
					<GroupChildrenView canEdit={false} children={group.children} />
					<GroupSourcesView canEdit={false} sources={group.sources} />
					<GroupPostersView canEdit={false} posters={group.posters} />
				</>
			);
		}
	}
};

export default GroupDetail;