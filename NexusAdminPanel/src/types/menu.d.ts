export type UmsMenu = {
  id?: number
  parentReviewId: number
  createdAt?: string
  title: string
  depth?: number
  displayOrder: number
  name: string
  iconUrl: string
  hidden: number
}

export type UmsMenuNode = UmsMenu & {
  children: UmsMenu[]
}
