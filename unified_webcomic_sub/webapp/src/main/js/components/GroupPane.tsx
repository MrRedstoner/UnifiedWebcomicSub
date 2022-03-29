'use strict';

import React, { useState } from 'react'
import GroupList from './GroupList'
import { UserPermissionClosure } from '../api/entities';
import GroupDetail from './GroupDetail';

type Props = {
	user: UserPermissionClosure;
}

const GroupPane: React.FC<Props> = ({ user }) => {
	const [detailId, setDetailId] = useState<number>(null)
	if (detailId == null) {
		return (
			<div>
				<h2>Groups</h2>
				<GroupList onRow={setDetailId} />
			</div>);
	} else {
		return (
			<div>
				<h2>Groups</h2>
				<GroupList onRow={setDetailId} />
				<GroupDetail id={detailId} user={user} />
			</div>);
	}
};

export default GroupPane;