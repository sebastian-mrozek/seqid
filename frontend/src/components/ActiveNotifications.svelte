<script lang="ts">
  import { activeNotifications, notifications } from "../stores/app-state";
  import { slide } from "svelte/transition";
</script>

<div class="container">
  {#each $activeNotifications as an}
    <div transition:slide class="notification {an.severity}">
      <button class="close-button small" on:click={() => notifications.deactivate(an.id)}>X</button>{an.text}
    </div>
  {/each}
</div>

<style>
  .container {
    position: fixed;
    top: 0px;
    right: 0px;
    padding: 1em;
    margin: 1em;
    z-index: 10000;
  }
  .notification {
    width: 200px;
    padding: 1em;
    margin-bottom: 1em;
    border-radius: 5px;
    border: 1px solid lightgray;
  }
  .close-button {
    float: right;
  }
  .ERR {
    background-color: lightcoral;
  }
  .WARN {
    background-color: lightyellow;
  }
  .INFO {
    background-color: lightseagreen;
  }
</style>
