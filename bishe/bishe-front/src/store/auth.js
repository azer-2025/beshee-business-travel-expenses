import { defineStore } from 'pinia'
import { getToken, removeToken, setToken } from '@/utils/auth'
import { getInfo, login, logout } from '@/api/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: getToken() || '',
    user: null
  }),
  actions: {
    async loginAction(form) {
      const res = await login(form)
      this.token = res.token
      setToken(res.token)
    },
    async fetchUser() {
      const res = await getInfo()
      this.user = res.user
      return res
    },
    async logoutAction() {
      try {
        await logout()
      } finally {
        this.token = ''
        this.user = null
        removeToken()
      }
    }
  }
})
