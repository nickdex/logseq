(ns frontend.modules.shortcut.config
  (:require [frontend.components.commit :as commit]
            [frontend.handler.config :as config-handler]
            [frontend.handler.editor :as editor-handler]
            [frontend.handler.history :as history]
            [frontend.handler.repo :as repo-handler]
            [frontend.handler.route :as route-handler]
            [frontend.handler.search :as search-handler]
            [frontend.handler.ui :as ui-handler]
            [frontend.handler.web.nfs :as nfs-handler]
            [frontend.modules.shortcut.before :as m]
            [frontend.state :as state]
            [frontend.util :refer [mac?]]))

(def default-config
  {:shortcut.handler/date-picker
   {:date-picker/complete
    {:desc    "Date picker choose selected day"
     :binding "enter"
     :fn      ui-handler/shortcut-complete}
    :date-picker/prev-day
    {:desc    "Date picker select previous day"
     :binding "left"
     :fn      ui-handler/shortcut-prev-day}
    :date-picker/next-day
    {:desc    "Date picker select next day"
     :binding "right"
     :fn      ui-handler/shortcut-next-day}
    :date-picker/prev-week
    {:desc    "Date picker select prev week"
     :binding "up"
     :fn      ui-handler/shortcut-prev-week}
    :date-picker/next-week
    {:desc    "Date picker select next week"
     :binding "down"
     :fn      ui-handler/shortcut-next-week}}

   :shortcut.handler/auto-complete
   {:auto-complete/prev
    {:desc    "Auto-complete previous selected item"
     :binding "up"
     :fn      ui-handler/auto-complete-prev}
    :auto-complete/next
    {:desc    "Auto-complete next selected item"
     :binding "down"
     :fn      ui-handler/auto-complete-next}
    :auto-complete/complete
    {:desc    "Auto-complete choose selected item"
     :binding "enter"
     :fn      ui-handler/auto-complete-complete}}

   :shortcut.handler/block-editing-only
   ^{:before m/enable-when-editing-mode!}
   {:editor/backspace
    {:desc    "Backspace / Delete backwards"
     :binding "backspace"
     :fn      editor-handler/editor-backspace}
    :editor/delete
    {:desc    "Delete / Delete forwards"
     :binding "delete"
     :fn      editor-handler/editor-delete}
    :editor/new-block
    {:desc    "Create new block"
     :binding "enter"
     :fn      editor-handler/keydown-new-block-handler}
    :editor/new-line
    {:desc    "Newline in block"
     :binding "shift+enter"
     :fn      editor-handler/keydown-new-line-handler}
    :editor/cycle-todo
    {:desc    "Rotate the TODO state of the current item"
     :binding "mod+enter"
     :fn      editor-handler/cycle-todo!}
    :editor/follow-link
    {:desc    "Follow link under cursor"
     :binding "mod+o"
     :fn      editor-handler/follow-link-under-cursor!}
    :editor/open-link-in-sidebar
    {:desc    "Open link in sidebar"
     :binding "mod+shift+o"
     :fn      editor-handler/open-link-in-sidebar!}
    :editor/bold
    {:desc    "Bold"
     :binding "mod+b"
     :fn      editor-handler/bold-format!}
    :editor/italics
    {:desc    "Italics"
     :binding "mod+i"
     :fn      editor-handler/italics-format!}
    :editor/highlight
    {:desc    "Highlight"
     :binding "mod+shift+h"
     :fn      editor-handler/highlight-format!}
    :editor/strike-through
    {:desc    "Strikethrough"
     :binding "mod+shift+s"
     :fn      editor-handler/strike-through-format!}
    :editor/insert-link
    {:desc    "Html Link"
     :binding "mod+k"
     :fn      editor-handler/html-link-format!}
    ;; FIXME
    ;; select-all-blocks only works in block editing mode
    ;; maybe we can improve this
    :editor/select-all-blocks
    {:desc    "Select all blocks"
     :binding "mod+shift+a"
     :fn      editor-handler/select-all-blocks!}
    :editor/move-block-up
    {:desc    "Move block up"
     :binding (if mac? "mod+shift+up"  "alt+shift+up")
     :fn      (editor-handler/move-up-down true)}
    :editor/move-block-down
    {:desc    "Move block down"
     :binding (if mac? "mod+shift+down" "alt+shift+down")
     :fn      (editor-handler/move-up-down false)}
    :editor/clear-block
    {:desc    "Clear entire block content"
     :binding (if mac? "ctrl+l" "alt+l")
     :fn      editor-handler/clear-block-content!}
    :editor/kill-line-before
    {:desc    "Kill line before cursor position"
     :binding (if mac? "ctrl+u" "alt+u")
     :fn      editor-handler/kill-line-before!}
    :editor/kill-line-after
    {:desc    "Kill line after cursor position"
     :binding (if mac? false "alt+k")
     :fn      editor-handler/kill-line-after!}
    :editor/beginning-of-block
    {:desc    "Move cursor to the beginning of block"
     :binding (if mac? false "alt+a")
     :fn      editor-handler/beginning-of-block}
    :editor/end-of-block
    {:desc    "Move cursor to the end of block"
     :binding (if mac? false "alt+e")
     :fn      editor-handler/end-of-block}
    :editor/forward-word
    {:desc    "Move cursor forward by word"
     :binding (if mac? "ctrl+shift+f" "alt+f")
     :fn      editor-handler/cursor-forward-word}
    :editor/backward-word
    {:desc    "Move cursor backward by word"
     :binding (if mac? "ctrl+shift+b" "alt+b")
     :fn      editor-handler/cursor-backward-word}
    :editor/forward-kill-word
    {:desc    "Kill a word forwards"
     :binding (if mac? "ctrl+w" "alt+d")
     :fn      editor-handler/forward-kill-word}
    :editor/backward-kill-word
    {:desc    "Kill a word backwards"
     :binding (if mac? false "alt+w")
     :fn      editor-handler/backward-kill-word}}

   :shortcut.handler/editor-global
   ^{:before m/enable-when-not-component-editing!}
   {:editor/up
    {:desc    "Move cursor up / Select up"
     :binding "up"
     :fn      (editor-handler/shortcut-up-down :up)}
    :editor/down
    {:desc    "Move cursor down / Select down"
     :binding "down"
     :fn      (editor-handler/shortcut-up-down :down)}
    :editor/left
    {:desc    "Move cursor left / Open selected block at beginning"
     :binding "left"
     :fn      (editor-handler/shortcut-left-right :left)}
    :editor/right
    {:desc    "Move cursor right / Open selected block at end"
     :binding "right"
     :fn      (editor-handler/shortcut-left-right :right)}
    ;; FIXME
    ;; add open edit in non-selection mode
    :editor/open-edit
    {:desc    "Edit selected block"
     :binding "enter"
     :fn      (partial editor-handler/open-selected-block! :right)}
    :editor/select-block-up
    {:desc    "Select block above"
     :binding "shift+up"
     :fn      (editor-handler/on-select-block :up)}
    :editor/select-block-down
    {:desc    "Select block below"
     :binding "shift+down"
     :fn      (editor-handler/on-select-block :down)}
    :editor/delete-selection
    {:desc    "Delete selected blocks"
     :binding ["backspace" "delete"]
     :fn      editor-handler/delete-selection}
    :editor/expand-block-children
    {:desc    "Expand"
     :binding "mod+down"
     :fn      editor-handler/expand!}
    :editor/collapse-block-children
    {:desc    "Collapse"
     :binding "mod+up"
     :fn      editor-handler/collapse!}
    :editor/collapse-top-blocks
    {:desc    "Collapse all blocks"
     :binding "mod+shift+l"
     :fn      editor-handler/collapse-all!}
    :editor/indent
    {:desc    "Indent block"
     :binding "tab"
     :fn      (editor-handler/keydown-tab-handler :right)}
    :editor/outdent
    {:desc    "Outdent block"
     :binding "shift+tab"
     :fn      (editor-handler/keydown-tab-handler :left)}
    :editor/copy
    {:desc    "Copy"
     :binding "mod+c"
     :fn      editor-handler/shortcut-copy}
    :editor/cut
    {:desc    "Cut"
     :binding "mod+x"
     :fn      editor-handler/shortcut-cut}
    :editor/undo
    {:desc    "Undo"
     :binding "mod+z"
     :fn      history/undo!}
    :editor/redo
    {:desc    "Redo"
     :binding ["shift+mod+z" "mod+y"]
     :fn      history/redo!}
    ;; FIXME
    ;; save in block editing only doesn't seems needed?
    :editor/save
    {:binding "mod+s"
     :fn      editor-handler/save!}}

   :shortcut.handler/global-prevent-default
   ^{:before m/prevent-default-behavior}
   {:editor/zoom-in
    {:desc    "Zoom in / Forward"
     :binding (if mac? "mod+." "alt+right")
     :fn      editor-handler/zoom-in!}
    :editor/zoom-out
    {:desc    "Zoom out / Back"
     :binding (if mac? "mod+," "alt+left")
     :fn      editor-handler/zoom-out!}
    :ui/toggle-brackets
    {:desc    "Toggle whether to display brackets"
     :binding "mod+c mod+b"
     :fn      config-handler/toggle-ui-show-brackets!}
    :go/search-in-page
    {:desc    "Search in the current page"
     :binding "mod+shift+u"
     :fn      #(route-handler/go-to-search! :page)}
    :go/search
    {:desc    "Full text search"
     :binding "mod+u"
     :fn      route-handler/go-to-search!}
    :go/journals
    {:desc    "Jump to journals"
     :binding (if mac? "mod+j" "alt+j")
     :fn      route-handler/go-to-journals!}
    :search/re-index
    {:desc    "Rebuild search index"
     :binding "mod+c mod+s"
     :fn      search-handler/rebuild-indices!}
    :graph/re-index
    {:desc    "Re-index the whole graph"
     :binding "mod+c mod+r"
     :fn      #(repo-handler/re-index! nfs-handler/rebuild-index!)}}

   :shortcut.handler/misc
   ;; always overrides the copy due to "mod+c mod+s"
   {:misc/copy
    {:binding "mod+c"
     :fn     (fn [] (js/document.execCommand "copy"))}}

   :shortcut.handler/global-non-editing-only
   ^{:before m/enable-when-not-editing-mode!}
   {:ui/toggle-document-mode
    {:desc    "Toggle document mode"
     :binding "t d"
     :fn      state/toggle-document-mode!}
    :ui/toggle-settings
    {:desc    "Toggle settings"
     :binding (if mac? "t s" ["t s" "mod+,"])
     :fn      ui-handler/toggle-settings-modal!}
    :ui/toggle-right-sidebar
    {:desc    "Toggle right sidebar"
     :binding "t r"
     :fn      ui-handler/toggle-right-sidebar!}
    :ui/toggle-help
    {:desc    "Toggle help"
     :binding "shift+/"
     :fn      ui-handler/toggle-help!}
    :ui/toggle-theme
    {:desc    "Toggle between dark/light theme"
     :binding "t t"
     :fn      state/toggle-theme!}
    :ui/toggle-new-block
    {:desc    "Toggle newblock/newline command for inserting newline/newblock"
     :binding "t e"
     :fn      state/toggle-new-block-shortcut!}
    :ui/toggle-contents
    {:desc    "Toggle Contents in sidebar"
     :binding "t c"
     :fn      ui-handler/toggle-contents!}
    :ui/toggle-wide-mode
    {:desc    "Toggle wide mode"
     :binding "t w"
     :fn      ui-handler/toggle-wide-mode!}
    ;; :ui/toggle-between-page-and-file route-handler/toggle-between-page-and-file!
    :git/commit
    {:desc    "Git commit message"
     :binding "g c"
     :fn      commit/show-commit-modal!}}})


;; Categories for docs purpose
(def category
  {:shortcut.category/basics
   ^{:doc "Basics"}
   [:editor/new-block
    :editor/new-line
    :editor/indent
    :editor/outdent
    :editor/collapse-top-blocks
    :editor/collapse-block-children
    :editor/expand-block-children
    :go/search
    :go/search-in-page
    :editor/undo
    :editor/redo
    :editor/zoom-in
    :editor/zoom-out
    :editor/copy
    :editor/cut
    :ui/toggle-wide-mode]

   :shortcut.category/formatting
   ^{:doc "Formatting"}
   [:editor/bold
    :editor/insert-link
    :editor/italics
    :editor/highlight]

   :shortcut.category/navigating
   ^{:doc "Navigation"}
   [:editor/up
    :editor/down
    :editor/left
    :editor/right]

   :shortcut.category/block-editing
   ^{:doc "Block editing general"}
   [:editor/backspace
    :editor/delete
    :editor/indent
    :editor/outdent
    :editor/new-block
    :editor/new-line
    :editor/zoom-in
    :editor/zoom-out
    :editor/cycle-todo
    :editor/follow-link
    :editor/open-link-in-sidebar
    :editor/select-all-blocks
    :editor/move-block-up
    :editor/move-block-down]

   :shortcut.category/block-command-editing
   ^{:doc "Block command editing"}
   [:editor/backspace
    :editor/clear-block
    :editor/kill-line-before
    :editor/kill-line-after
    :editor/beginning-of-block
    :editor/end-of-block
    :editor/forward-word
    :editor/backward-word
    :editor/forward-kill-word
    :editor/backward-kill-word]

   :shortcut.category/block-selection
   ^{:doc "Block selection (press Esc to quit selection)"}
   [:editor/open-edit
    :editor/select-block-up
    :editor/select-block-down
    :editor/delete-selection]

   :shortcut.category/toggle
   ^{:doc "Toggle"}
   [:ui/toggle-help
    :ui/toggle-new-block
    :ui/toggle-wide-mode
    :ui/toggle-document-mode
    :ui/toggle-brackets
    :ui/toggle-theme
    :ui/toggle-right-sidebar
    :ui/toggle-settings
    :ui/toggle-contents]

   :shortcut.category/others
   ^{:doc "Others"}
   [:go/journals
    :search/re-index
    :graph/re-index
    :auto-complete/prev
    :auto-complete/next
    :auto-complete/complete
    :date-picker/prev-day
    :date-picker/next-day
    :date-picker/prev-week
    :date-picker/next-week
    :date-picker/complete]})
