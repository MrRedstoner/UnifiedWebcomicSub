'use strict';

import React from 'react';

type HeaderItem = {
	key: string;
	field: (obj:any)=>any;
	content: JSX.Element;
}

type HeaderProps = {
	columns: HeaderItem[];
}

const TableHeader: React.FC<HeaderProps> = ({ columns }) => {
	return (<thead>
		<tr>
			{columns.map(col => <th key={col.key}>{col.content}</th>)}
		</tr>
	</thead>);
}

type RowProps = {
	columns: HeaderItem[];
	item: any;
	onClick: () => void;
}

const TableRow: React.FC<RowProps> = ({ columns, item, onClick }) => {
	return (<tr>
		{columns.map(col => <td key={col.key} onClick={onClick}>{col.field(item)}</td>)}
	</tr>);
}

type Props = {
	columns: HeaderItem[];
	isLoaded: boolean;
	error: string;
	items: any[];
	onRow: (item: any) => void;
}

const renderRows = (columns: HeaderItem[], error: string, isLoaded: boolean, items: any[], onRow: (item: any) => void) => {
	if (error) {
		return <tr><td colSpan={columns.length} className="allignCenter">Error: {error}</td></tr>;
	} else if (!isLoaded) {
		return <tr><td colSpan={columns.length} className="allignCenter">Loading...</td></tr>;
	} else {
		return <>{items.map((item, i) => <TableRow key={i} item={item} columns={columns} onClick={() => onRow(item)} />)}</>
	}
}

const SimpleTable: React.FC<Props> = ({ columns, isLoaded, error, items, onRow }) => {
	return (<table>
		<TableHeader columns={columns} />
		<tbody>
			{renderRows(columns, error, isLoaded, items, onRow)}
		</tbody>
	</table>);
}

export type { HeaderItem };
export default SimpleTable;