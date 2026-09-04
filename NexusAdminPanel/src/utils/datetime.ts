import { dayjs } from 'element-plus'

export const formatDateTime = (time: string) => {
  if (!time) {
    return 'N/A'
  }
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}

export const formatDate = (time: string) => {
  if (!time) {
    return 'N/A'
  }
  return dayjs(time).format('YYYY-MM-DD')
}

export const formatTime = (time: string) => {
  if (!time) {
    return 'N/A'
  }
  return dayjs(time).format('HH:mm:ss')
}

export function str2Date(dateStr: string, separator?: string) {
  if (!separator) {
    separator = '-'
  }
  const dateArr = dateStr.split(separator)
  if (!dateArr[0] || !dateArr[1] || !dateArr[2]) return undefined
  const year = parseInt(dateArr[0])
  let month
  if (dateArr[1].indexOf('0') == 0) {
    month = parseInt(dateArr[1].substring(1))
  } else {
    month = parseInt(dateArr[1])
  }
  const day = parseInt(dateArr[2])
  const date = new Date(year, month - 1, day)
  return date
}
