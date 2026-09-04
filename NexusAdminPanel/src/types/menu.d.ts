export type UmsMenu = {
  id?: number
  parentId: number
  createTime?: string
  title: string
  level?: number
  sort: number
  name: string
  icon: string
  hidden: number
}

export type UmsMenuNode = UmsMenu & {
  children: UmsMenu[]
}
