'use strict';

import React, { useState } from 'react'
import GroupList from './GroupList'
import { UserPermissionClosure } from '../api/entities';
import GroupDetail from './GroupDetail';
import GroupCreate from './GroupCreate';

type Props = {
	user: UserPermissionClosure;
}

type DetailOpen = {
	id: number;
}

const GroupPane: React.FC<Props> = ({ user }) => {
	const [detail, setDetail] = useState<DetailOpen>(null);

	const setDetailId = (id: number) => {
		setDetail({ id: id });
	}

	const onNew = () => {
		setDetail({ id: null });
	}

	const newBtn = <button disabled={!user.editGroup} onClick={onNew}>New</button>

	if (detail == null) {
		return (
			<div>
				<h2>Groups</h2>
				{newBtn}
				<GroupList onRow={setDetailId} />
			</div>);
	} else {
		if (detail.id == null) {
			return (
				<div>
					<h2>Groups</h2>
					{newBtn}
					<GroupList onRow={setDetailId} />
					<GroupCreate user={user} onCreated={setDetailId} />
				</div>);
		} else {
			return (
				<div>
					<h2>Groups</h2>
					{newBtn}
					<GroupList onRow={setDetailId} />
					<GroupDetail id={detail.id} user={user} />
				</div>);
		}
	}
};

export default GroupPane;