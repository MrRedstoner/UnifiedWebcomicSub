'use strict';

import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from "react-router-dom";
import { UWSUser, UserPermissionClosure } from '../../api/entities';
import { USER_SERVICE_MODERATORS_READ_DETAIL, USER_SERVICE_MODERATORS_UPDATE_SUBSCRIBE } from '../../api/apiEndpoints';
import { asyncFetchGetState, asyncFetchPostState } from '../../api/apiCall';

const makeSubButton = (user: UserPermissionClosure, moderator: UWSUser, updateSub: (value: boolean) => void, updateIgnore: (value: boolean) => void) => {
	if (moderator.ignored) {
		return (<button disabled={!user.registered} onClick={async () => updateIgnore(false)}>Unignore</button>);
	} else if (!moderator.subscribed) {
		return (<>
			<button disabled={!user.registered} onClick={async () => updateSub(true)}>Subscribe</button>
			<button disabled={!user.registered} onClick={async () => updateIgnore(true)}>Ignore</button>
		</>);
	} else {
		return (<button disabled={!user.registered} onClick={async () => updateSub(false)}>Unsubscribe</button>);
	}
}

type ItemState = {
	isLoaded: boolean;
	error: string | null;
	item: UWSUser;
}

const notLoaded: ItemState = { isLoaded: false, error: null, item: null }

type Props = {
	user: UserPermissionClosure;
}

const ModeratorDetail: React.FC<Props> = ({ user }) => {
	const navigate = useNavigate();
	const { id } = useParams();
	const onBack = () => { navigate("/mods"); };
	const [itemState, setItemState] = useState<ItemState>(notLoaded);

	const loadData = () => {
		asyncFetchGetState(
			USER_SERVICE_MODERATORS_READ_DETAIL,
			{ id: id },
			(item: UWSUser) => setItemState({ isLoaded: true, error: null, item: item }),
			error => setItemState({ isLoaded: true, error: error, item: null }));
	}

	useEffect(() => {
		loadData();
	}, [id]);

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
		const updateSubImpl = async (sub: boolean, value: boolean) => {
			setItemState(notLoaded);
			const data = {
				id: id.toString(),
				subscribe: sub.toString(),
				value: value.toString()
			};
			await asyncFetchPostState(
				USER_SERVICE_MODERATORS_UPDATE_SUBSCRIBE,
				data,
				(item: UWSUser) => setItemState({ isLoaded: true, error: null, item: item }),
				error => setItemState({ isLoaded: true, error: error, item: null }));
		}

		const subButton = makeSubButton(user, itemState.item, value => updateSubImpl(true, value), value => updateSubImpl(false, value));

		return (<>
			<button onClick={onBack}>Back to list</button>
			<h3>Moderator {itemState.item.id}</h3>
			<p>Name: {itemState.item.name}</p>
			{subButton}
		</>);
	}
}

export default ModeratorDetail;