export interface CarModel {
  id: number
  model: string
  brand: string
  carTitle: string
  year: number
  status: 'AVAILABLE' | 'RENTED' | 'IN_MAINTENANCE',
  engine: string
  accessories: string[]
}
